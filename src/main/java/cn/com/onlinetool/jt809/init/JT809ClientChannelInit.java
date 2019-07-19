package cn.com.onlinetool.jt809.init;

import cn.com.onlinetool.jt809.handler.inbound.Byte2MessageInboundHandler;
import cn.com.onlinetool.jt809.handler.inbound.MessageForwardInboundHandler;
import cn.com.onlinetool.jt809.handler.outbound.Message2ByteOutboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author choice
 * @date 2019-07-17 19:09
 *
 */
@Component
public class JT809ClientChannelInit extends ChannelInitializer<SocketChannel> {
    @Autowired
    Message2ByteOutboundHandler message2ByteOutboundHandler;
    @Autowired
    Byte2MessageInboundHandler byte2MessageInboundHandler;
    @Autowired
    MessageForwardInboundHandler messageForwardInboundHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new IdleStateHandler(20,0,0));
        socketChannel.pipeline().addLast(message2ByteOutboundHandler);
        socketChannel.pipeline().addLast(byte2MessageInboundHandler);
        socketChannel.pipeline().addLast(messageForwardInboundHandler);
    }
}
