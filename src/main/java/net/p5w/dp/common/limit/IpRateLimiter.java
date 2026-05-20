package net.p5w.dp.common.limit;

import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.p5w.dp.config.IpLimitConfig;

@Slf4j
@Component
public class IpRateLimiter {

    @Resource
    private IpLimitConfig ipLimitConfig;

    // 滑动窗口：IP -> 请求时间戳队列
    private static final ConcurrentHashMap<String, Deque<Long>> WINDOW = new ConcurrentHashMap<>();

    // 定时清理空队列，防止内存泄漏
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r, "ip-limit-clean-task");
        thread.setDaemon(true);
        return thread;
    });

    static {
        log.info("滑动窗口限流初始化，定时清理任务启动");
        scheduler.scheduleAtFixedRate(
                () -> WINDOW.entrySet().removeIf(entry -> entry.getValue().isEmpty()),
                10, 10, TimeUnit.MINUTES
        );
    }

    /**
     * 滑动窗口限流：任意 60 秒内最多允许 maxPerMinute 次请求
     */
    public boolean tryAcquire(String ip) {
        if (!ipLimitConfig.isEnabled()) {
            log.debug("IP限流已关闭，直接放行");
            return true;
        }

        if (ip == null || ip.isEmpty()) {
            log.warn("IP为空，限流拦截");
            return false;
        }

        long now = System.currentTimeMillis();
        long windowStart = now - 60 * 1000; // 60秒窗口
        int max = ipLimitConfig.getMaxPerMinute();

        Deque<Long> deque = WINDOW.computeIfAbsent(ip, k -> new LinkedBlockingDeque<>());

        synchronized (deque) {
            // 清除超过 60 秒的旧时间戳
            while (!deque.isEmpty() && deque.peekFirst() < windowStart) {
                deque.pollFirst();
            }

            // 判断是否超限
            if (deque.size() >= max) {
                log.warn("IP[{}] 限流触发：60秒内访问 {} 次，上限 {}", ip, deque.size(), max);
                return false;
            }

            // 添加当前时间戳
            deque.offerLast(now);
            log.debug("IP[{}] 允许访问：60秒内 {} 次", ip, deque.size());
            return true;
        }
    }

    /**
     * 优雅关闭线程池
     */
    @PreDestroy
    public void destroy() {
        log.info("IP限流服务关闭，释放线程池...");
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(3, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}