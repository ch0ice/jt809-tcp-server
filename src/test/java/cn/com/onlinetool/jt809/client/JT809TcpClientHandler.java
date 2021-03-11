package cn.com.onlinetool.jt809.client;

import cn.com.onlinetool.jt809.util.ByteArrayUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author choice
 * @description:
 * @date 2019-03-13 15:02
 *
 */
public class JT809TcpClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive..........");
        ctx.channel().write(ByteArrayUtil.hexStr2Bytes(
                "5B000000480000005210010001E24001000100000000000001E2407465737438303900312E37312E3132392E32303100000000000000000000000000000000000000004E8ED9BA5D"
        ));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead.........." + ByteArrayUtil.bytes2HexStr((byte[])msg));
        super.channelRead(ctx,msg);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exceptionCaught..........");
        cause.printStackTrace();
        ctx.close();
    }
}
