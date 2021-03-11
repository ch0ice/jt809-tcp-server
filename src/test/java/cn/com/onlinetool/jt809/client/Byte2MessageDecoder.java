package cn.com.onlinetool.jt809.client;


import cn.com.onlinetool.jt809.util.ByteArrayUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


/**
 * @author choice
 * @description: 消息解码
 * @date 2018-12-27 14:39
 *
 */
@Slf4j
public class Byte2MessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        byte[] readDatas = new byte[msg.readableBytes()];
        msg.readBytes(readDatas);
        log.info("接收到服务端发送的下行消息：{}", ByteArrayUtil.bytes2HexStr(readDatas));
    }
}
