package cn.com.onlinetool.jt809.encoder;

import cn.com.onlinetool.jt809.bean.Message;
import cn.com.onlinetool.jt809.util.PacketUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author choice
 * @description: 编码
 * @date 2018-12-27 15:09
 *
 */
@Slf4j
@Service
public class Message2ByteEncoder {
    public void encode(ChannelHandlerContext ctx, Message msg) throws Exception {
        ByteBuf byteBuf = ctx.alloc().buffer();
        byteBuf.writeBytes(PacketUtil.message2Bytes(msg));
        ctx.write(byteBuf);
    }
}
