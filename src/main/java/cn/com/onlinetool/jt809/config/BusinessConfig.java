package cn.com.onlinetool.jt809.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private Integer downlinkMsgExpire = 300;

    /**
     * 业务处理核心线程数
     */
    private Integer corePoolSize = 4;

    /**
     * 业务处理最大线程数
     */
    private Integer maximumPoolSize = 8;

    /**
     * 业务处理线程超时时间 秒
     */
    private Integer keepAliveTime = 10;

    /**
     * kafka输出topic
     */
    private String outputKafkaTopic;

    /**
     * 是否启动从链路 默认不启动
     */
    private Boolean isOpenClient = false;
}
