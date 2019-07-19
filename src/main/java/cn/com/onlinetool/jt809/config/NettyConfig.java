package cn.com.onlinetool.jt809.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author choice
 * @description: netty配置信息
 * @date 2018-12-27 12:20
 *
 */
public class NettyConfig {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Component
    @ConfigurationProperties(prefix = "netty.server")
    public static class NettyServerConfig{
        /**
         * netty server port
         */
        private Integer tcpPort;

        /**
         * 主线程最大线程数
         */
        private Integer bossMaxThreadCount = 4;

        /**
         * 工作线程最大线程数
         */
        private Integer workMaxThreadCount = 8;

        /**
         * 数据包最大长度
         */
        private Integer maxFrameLength = 65535;

        /**
         * 单节点最大连接数
         */
        private Integer maxConnectNum;

        /**
         * 读事件空闲时间 秒
         */
        private Integer readerIdleTimeSeconds = 60;

        /**
         * 写事件空闲时间 秒
         */
        private Integer writerIdleTimeSeconds = 60;
        /**
         * 读写都空闲事件 秒
         */
        private Integer allIdleTimeSeconds = 60;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Component
    @ConfigurationProperties(prefix = "netty.client")
    public static class NettyClientConfig{
        /**
         * netty client ip
         */
        private String tcpIp;
        /**
         * netty client port
         */
        private Integer tcpPort;
        /**
         * 读事件空闲时间 秒
         */
        private Integer readerIdleTimeSeconds = 60;

        /**
         * 写事件空闲时间 秒
         */
        private Integer writerIdleTimeSeconds = 60;
        /**
         * 读写都空闲事件 秒
         */
        private Integer allIdleTimeSeconds = 60;
    }



}
