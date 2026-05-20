package net.p5w.dp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "ip.limit")
public class IpLimitConfig {
    // 是否开启限流
    private boolean enabled = true;

    // 每分钟最大请求数
    private int maxPerMinute = 20;
}