package cn.com.onlinetool.jt809.encoder;

import cn.com.onlinetool.jt809.bean.Message;
import cn.com.onlinetool.jt809.util.ByteArrayUtil;
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
        //headFlag
        byteBuf.writeByte(msg.getHeadFlag());
        //dataHead
        byte[] msgLength = ByteArrayUtil.int2bytes(msg.getMsgHead().getMsgLength());
        byteBuf.writeBytes(msgLength);
        byte[] msgSn = ByteArrayUtil.int2bytes(msg.getMsgHead().getMsgSn());
        byteBuf.writeBytes(msgSn);
        byte[] msgId = ByteArrayUtil.short2Bytes(msg.getMsgHead().getMsgId());
        byteBuf.writeBytes(msgId);
        byte[] msgGnssCenterId = ByteArrayUtil.int2bytes(msg.getMsgHead().getMsgGnssCenterId());
        byteBuf.writeBytes(msgGnssCenterId);
        byteBuf.writeBytes(msg.getMsgHead().getVersionFlag());
        byteBuf.writeByte(msg.getMsgHead().getEncryptFlag());
        byte[] encrypyKey = ByteArrayUtil.int2bytes(msg.getMsgHead().getEncryptKey());
        byteBuf.writeBytes(encrypyKey);
        //dataBody
        byteBuf.writeBytes(msg.getMsgBody());
        //crc
        byteBuf.writeBytes(msg.getCrcCode());
        //endFlag
        byteBuf.writeByte(msg.getEndFlag());
        log.info("downside by message...");
    }
}
