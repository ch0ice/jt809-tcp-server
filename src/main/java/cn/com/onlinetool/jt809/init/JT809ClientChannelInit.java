package cn.com.onlinetool.jt809.init;

import cn.com.onlinetool.jt809.handler.outbound.Message2ByteOutboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
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

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(message2ByteOutboundHandler);
    }
}
