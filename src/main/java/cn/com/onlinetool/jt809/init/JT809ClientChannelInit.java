package cn.com.onlinetool.jt809.init;

import cn.com.onlinetool.jt809.config.NettyConfig;
import cn.com.onlinetool.jt809.handler.inbound.ClientByte2MessageInboundHandler;
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
 * @date 2019-07-17 19:09
 *
 */
@Component
public class JT809ClientChannelInit extends ChannelInitializer<SocketChannel> {
    @Autowired
    NettyConfig.NettyClientConfig clientConfig;
    @Autowired
    Message2ByteOutboundHandler message2ByteOutboundHandler;
    @Autowired
    ClientByte2MessageInboundHandler clientByte2MessageInboundHandler;
    @Autowired
    MessageForwardInboundHandler messageForwardInboundHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new IdleStateHandler(clientConfig.getReaderIdleTimeSeconds(),clientConfig.getWriterIdleTimeSeconds(),clientConfig.getAllIdleTimeSeconds(), TimeUnit.SECONDS));
        socketChannel.pipeline().addLast(message2ByteOutboundHandler);
        socketChannel.pipeline().addLast(clientByte2MessageInboundHandler);
        socketChannel.pipeline().addLast(messageForwardInboundHandler);
    }
}
