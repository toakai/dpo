package net.p5w.dp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * IP 限流配置
 * <p>
 * 对应配置文件中的 {@code ip.limit.*} 前缀。
 * 所有字段均可通过 application.yml 或环境 profile 覆盖。
 * </p>
 */
@Data
@Component
@ConfigurationProperties(prefix = "ip.limit")
public class IpLimitConfig {

    /** 是否开启限流 */
    private boolean enabled = true;

    /** 滑动窗口内每 IP 最大请求数 */
    private int maxPerMinute = 5;

    /** 滑动窗口大小（秒） */
    private int windowSize = 60;

    /** 空队列清理间隔（分钟） */
    private int cleanInterval = 10;
}