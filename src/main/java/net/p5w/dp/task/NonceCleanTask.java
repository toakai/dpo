package net.p5w.dp.task;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.p5w.dp.service.NonceService;

/**
 * Nonce 过期记录定时清理任务
 * <p>
 * 定期删除 {@code p5w_nonce} 表中超过有效期的记录，防止表无限膨胀。
 * 清理的时间阈值 = 当前时间 - nonce 有效期（{@code auth.expire-time}），与签名校验的过期时间保持一致。
 * </p>
 */
@Slf4j
@Component
public class NonceCleanTask {

    @Resource
    private NonceService nonceService;

    /** nonce 有效期（毫秒），与签名校验过期时间一致 */
    @Value("${auth.expire-time:300000}")
    private long expireTime;

    /**
     * 每小时执行一次清理
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void cleanExpiredNonce() {
        try {
            Date threshold = new Date(System.currentTimeMillis() - expireTime);
            int deleted = nonceService.deleteByCreateTimeBefore(threshold);
            if (deleted > 0) {
                log.info("清理过期 nonce 完成，删除 {} 条记录", deleted);
            }
        } catch (Exception e) {
            log.error("清理过期 nonce 失败", e);
        }
    }
}
