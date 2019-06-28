package cn.com.onlinetool.jt809.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author choice
 * @description:
 * @date 2019-01-21 17:43
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "business")
public class BusinessConfig {
    /**
     * 下行消息缓存超时时间
     */
    @Value("${downlink-msg-expire : 300}")
    private Integer downlinkMsgExpire;

    /**
     * 业务处理核心线程数
     */
    @Value("${core-pool-size : 4}")
    private Integer corePoolSize;

    /**
     * 业务处理最大线程数
     */
    @Value("${maximum-pool-size : 8}")
    private Integer maximumPoolSize;

    /**
     * 业务处理线程超时时间 秒
     */
    @Value("${keep-alive-time : 10}")
    private Integer keepAliveTime;
}
