package cn.com.onlinetool.jt809.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author choice
 * @description: netty配置信息
 * @date 2018-12-27 12:20
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "netty")
public class NettyConfig {

    /**
     * netty server port
     */
    @Value("${tcp-port : 11111}")
    public Integer tcpPort;

    /**
     * 主线程最大线程数
     */
    @Value("${boss-max-thread-count : 4}")
    public Integer bossMaxThreadCount;

    /**
     * 工作线程最大线程数
     */
    @Value("${work-max-thread-count : 8}")
    public Integer workMaxThreadCount;

    /**
     * 数据包最大长度
     */
    @Value("${max-frame-length : 65535}")
    public Integer maxFrameLength;

    /**
     * 单节点最大连接数
     */
    @Value("${max-connect-num : 0}")
    public Integer maxConnectNum;

    /**
     * 客户端超时时间，如果空闲时间大于此值，关闭连接，单位秒
     */
    @Value("${reader-idle-time-seconds : 60}")
    public Integer readerIdleTimeSeconds;
}
