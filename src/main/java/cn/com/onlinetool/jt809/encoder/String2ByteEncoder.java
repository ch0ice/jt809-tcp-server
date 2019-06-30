package cn.com.onlinetool.jt809.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author choice
 * @description:
 * @date 2019-06-30 19:28
 *
 */
@Slf4j
public class String2ByteEncoder extends MessageToByteEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String message, ByteBuf byteBuf) throws Exception {
        log.info("upside by string");
    }
}
