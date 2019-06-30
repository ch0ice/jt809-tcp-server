package cn.com.onlinetool.jt809.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author choice
 * @description: 编码
 * @date 2018-12-27 15:09
 *
 */
public class Message2ByteEncoder extends MessageToByteEncoder<Object> {
    @Override
    public void encode(ChannelHandlerContext ctx, Object msg, ByteBuf byteBuf) throws Exception {

    }
}
