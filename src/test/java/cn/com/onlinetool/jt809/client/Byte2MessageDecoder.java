package cn.com.onlinetool.jt809.client;


import cn.com.onlinetool.jt809.util.ByteArrayUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * @author choice
 * @description: 消息解码
 * @date 2018-12-27 14:39
 *
 */
public class Byte2MessageDecoder extends ByteToMessageDecoder {
    private Logger logger = LoggerFactory.getLogger(Byte2MessageDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        byte[] readDatas = new byte[msg.readableBytes()];
        msg.readBytes(readDatas);
        logger.info("接收到服务端发送的下行消息：{}", ByteArrayUtil.bytes2HexStr(readDatas));

    }
}
