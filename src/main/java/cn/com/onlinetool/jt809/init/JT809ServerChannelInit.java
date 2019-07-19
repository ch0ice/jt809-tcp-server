package cn.com.onlinetool.jt809.init;

import cn.com.onlinetool.jt809.config.NettyConfig;
import cn.com.onlinetool.jt809.handler.inbound.ServerByte2MessageInboundHandler;
import cn.com.onlinetool.jt809.handler.inbound.MessageForwardInboundHandler;
import cn.com.onlinetool.jt809.handler.outbound.Message2ByteOutboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author choice
 * netty server 责任链配置
 * @date 2018-12-27 13:14
 *
 */
@Component
public class JT809ServerChannelInit extends ChannelInitializer<SocketChannel> {
    @Autowired
    NettyConfig.NettyServerConfig serverConfig;
    @Autowired
    ServerByte2MessageInboundHandler serverByte2MessageInboundHandler;
    @Autowired
    MessageForwardInboundHandler messageForwardInboundHandler;
    @Autowired
    Message2ByteOutboundHandler message2ByteOutboundHandler;


    @Override
    public void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new IdleStateHandler(serverConfig.getReaderIdleTimeSeconds(),serverConfig.getWriterIdleTimeSeconds(),serverConfig.getAllIdleTimeSeconds(), TimeUnit.SECONDS));
        socketChannel.pipeline().addLast(message2ByteOutboundHandler);
        socketChannel.pipeline().addLast(serverByte2MessageInboundHandler);
        socketChannel.pipeline().addLast(messageForwardInboundHandler);
    }
}
