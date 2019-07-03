package cn.com.onlinetool.jt809.server;

import cn.com.onlinetool.jt809.config.NettyConfig;
import cn.com.onlinetool.jt809.init.JT809ServerChannelInit;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author choice
 * @description: netty启动类
 * @date 2018-12-27 12:52
 *
 */
@Slf4j
@Service
public class JT809Server {
    @Autowired
    private NettyConfig nettyConfig;
    @Autowired
    private JT809ServerChannelInit JT809ServerChannelInit;

    public void runServer() throws Exception{
        //创建主线程池（接收线程池）
        EventLoopGroup boosGroup = new NioEventLoopGroup(nettyConfig.getBossMaxThreadCount(),new DefaultThreadFactory("boosServer",true));
        //创建工作线程池
        EventLoopGroup workGroup = new NioEventLoopGroup(nettyConfig.getWorkMaxThreadCount(),new DefaultThreadFactory("workServer",true));

        try {
            //创建一个服务器端的程序类进行NIO的启动，同时可以设置Channel
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //设置要使用的线程池以及当前的Channel 的类型
            serverBootstrap.group(boosGroup,workGroup).channel(NioServerSocketChannel.class);
            serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
            //接收到的信息处理器
            serverBootstrap.childHandler(JT809ServerChannelInit);
            serverBootstrap.option(ChannelOption.SO_BACKLOG,1024);
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE,true);
            //ChannelFuture描述异步回调的处理操作
            ChannelFuture future = serverBootstrap.bind(nettyConfig.getTcpPort()).sync();

            log.info("nettyServer run success,TCP-PORT:{}",nettyConfig.getTcpPort());

            //等待socket被关闭
            future.channel().closeFuture().sync();
        } catch (Exception e){
            log.error("nettyServer run fail");
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
            boosGroup.shutdownGracefully();
        }

    }
}
