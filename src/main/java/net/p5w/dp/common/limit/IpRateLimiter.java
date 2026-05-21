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

/**
 * 基于滑动窗口的 IP 限流器
 * <p>
 * 统计任意 60 秒内同一 IP 的请求次数，超过 {@link IpLimitConfig#getMaxPerMinute()} 则拒绝请求。
 * 使用 {@link ConcurrentHashMap} 存储各 IP 的时间戳队列，定时清理空队列防止内存泄漏。
 * </p>
 */
@Slf4j
@Component
public class IpRateLimiter {

    @Resource
    private IpLimitConfig ipLimitConfig;

    /** IP → 请求时间戳滑动窗口队列 */
    private static final ConcurrentHashMap<String, Deque<Long>> WINDOW = new ConcurrentHashMap<>();

    /** 定时清理空队列，防止长尾 IP 占用内存 */
    private static final ScheduledExecutorService CLEANER = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r, "ip-limit-cleaner");
        thread.setDaemon(true);
        return thread;
    });

    static {
        CLEANER.scheduleAtFixedRate(
                () -> WINDOW.entrySet().removeIf(entry -> entry.getValue().isEmpty()),
                10, 10, TimeUnit.MINUTES
        );
        log.info("IP 限流器初始化完成，空队列清理任务已启动");
    }

    /**
     * 尝试获取请求许可
     *
     * @param ip 客户端 IP
     * @return true 允许通过，false 触发限流
     */
    public boolean tryAcquire(String ip) {
        if (!ipLimitConfig.isEnabled()) {
            return true;
        }

        if (ip == null || ip.isEmpty()) {
            log.warn("IP 为空，拒绝请求");
            return false;
        }

        long now = System.currentTimeMillis();
        long windowStart = now - 60 * 1000L; // 60 秒滑动窗口
        int max = ipLimitConfig.getMaxPerMinute();

        Deque<Long> deque = WINDOW.computeIfAbsent(ip, k -> new LinkedBlockingDeque<>());

        synchronized (deque) {
            // 清除窗口外的过期时间戳
            while (!deque.isEmpty() && deque.peekFirst() < windowStart) {
                deque.pollFirst();
            }

            if (deque.size() >= max) {
                log.warn("IP[{}] 触发限流：60s 内请求 {} 次，上限 {}", ip, deque.size(), max);
                return false;
            }

            deque.offerLast(now);
            log.debug("IP[{}] 允许访问：60s 内已请求 {} 次", ip, deque.size());
            return true;
        }
    }

    /**
     * 优雅关闭清理线程池（Spring 容器销毁时自动调用）
     */
    @PreDestroy
    public void destroy() {
        log.info("IP 限流器关闭，释放清理线程池");
        CLEANER.shutdown();
        try {
            if (!CLEANER.awaitTermination(3, TimeUnit.SECONDS)) {
                CLEANER.shutdownNow();
            }
        } catch (InterruptedException e) {
            CLEANER.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
