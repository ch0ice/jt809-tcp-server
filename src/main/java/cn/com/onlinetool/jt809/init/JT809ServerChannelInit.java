package cn.com.onlinetool.jt809.init;

import cn.com.onlinetool.jt809.config.NettyConfig;
import cn.com.onlinetool.jt809.decoder.Byte2MessageDecoder;
import cn.com.onlinetool.jt809.encoder.Message2ByteEncoder;
import cn.com.onlinetool.jt809.encoder.String2ByteEncoder;
import cn.com.onlinetool.jt809.handler.JT809ServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author choice
 * @description: netty server 责任链配置
 * @date 2018-12-27 13:14
 *
 */
@Component
public class JT809ServerChannelInit extends ChannelInitializer<SocketChannel> {
    @Autowired
    NettyConfig nettyConfig;
    @Autowired
    JT809ServerHandler jt809ServerHandler;


    @Override
    public void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new IdleStateHandler(nettyConfig.getReaderIdleTimeSeconds(),0,0));
        socketChannel.pipeline().addLast(new Message2ByteEncoder());
        socketChannel.pipeline().addLast(new String2ByteEncoder());
        socketChannel.pipeline().addLast(new Byte2MessageDecoder());
        socketChannel.pipeline().addLast(jt809ServerHandler);
    }
}
