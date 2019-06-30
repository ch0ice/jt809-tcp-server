package cn.com.onlinetool.jt809.encoder;

import cn.com.onlinetool.jt809.bean.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author choice
 * @description: 编码
 * @date 2018-12-27 15:09
 *
 */
@Slf4j
public class Message2ByteEncoder extends MessageToByteEncoder<Message> {
    @Override
    public void encode(ChannelHandlerContext ctx, Message msg, ByteBuf byteBuf) throws Exception {
        log.info("downside by message...");
    }
}
