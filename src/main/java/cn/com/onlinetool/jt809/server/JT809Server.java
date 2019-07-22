package cn.com.onlinetool.jt809.server;

import cn.com.onlinetool.jt809.config.BusinessConfig;
import cn.com.onlinetool.jt809.config.NettyConfig;
import cn.com.onlinetool.jt809.init.JT809ClientChannelInit;
import cn.com.onlinetool.jt809.init.JT809ServerChannelInit;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
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
    private NettyConfig.NettyServerConfig serverConfig;
    @Autowired
    private BusinessConfig businessConfig;
    @Autowired
    private JT809ServerChannelInit JT809ServerChannelInit;

    /**
     * 主链路（服务端）引导入口
     * @throws Exception
     */
    public void runServer() throws Exception{
        //创建主线程池（接收线程池）
        EventLoopGroup boosGroup = new NioEventLoopGroup(serverConfig.getBossMaxThreadCount(),new DefaultThreadFactory("boosServer",true));
        //创建工作线程池
        EventLoopGroup workGroup = new NioEventLoopGroup(serverConfig.getWorkMaxThreadCount(),new DefaultThreadFactory("workServer",true));

        try {
            //创建一个服务器端的程序类进行NIO的启动，同时可以设置Channel
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //设置要使用的线程池以及当前的Channel 的类型
            serverBootstrap.group(boosGroup,workGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
            //接收到的信息处理器
            serverBootstrap.childHandler(JT809ServerChannelInit);
            serverBootstrap.option(ChannelOption.SO_BACKLOG,128);
            serverBootstrap.option(ChannelOption.TCP_NODELAY, true);
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE,true);
            //ChannelFuture描述异步回调的处理操作
            ChannelFuture future = serverBootstrap.bind(serverConfig.getTcpPort()).sync();

            log.info("nettyServer run success,TCP-PORT:{}",serverConfig.getTcpPort());
            if(businessConfig.getIsOpenClient()){
                //启动从链路
                this.runClient(future.channel().eventLoop());
            }
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




    @Autowired
    private NettyConfig.NettyClientConfig clientConfig;
    @Autowired
    private JT809ClientChannelInit jt809ClientChannelInit;
    public static Channel clientChannel;

    /**
     * 从链路（客户端）引导入口
     * @param group
     * @throws Exception
     */
    private void runClient(EventLoopGroup group) throws Exception {
        String ip = clientConfig.getTcpIp();
        Integer port = clientConfig.getTcpPort();
        try {
            Bootstrap client = new Bootstrap();
            client.group(group);
            client.channel(NioSocketChannel.class);
            client.option(ChannelOption.TCP_NODELAY, true);
            client.handler(jt809ClientChannelInit);
            ChannelFuture channelFuture = client.connect(ip, port).sync();
            channelFuture.addListener(new GenericFutureListener() {
                @Override
                public void operationComplete(Future future) throws Exception {
                    if (future.isSuccess()) {
                        log.info("nettyClient run success,TCP-IP:{},TCP-PORT:{}",ip,port);
                        clientChannel = channelFuture.channel();
                    }
                }
            });
        }catch (Exception e){
            log.error("nettyClient run fail");
            e.printStackTrace();
        }
    }


}
