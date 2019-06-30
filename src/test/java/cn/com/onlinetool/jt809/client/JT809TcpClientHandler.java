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
        ctx.writeAndFlush(ByteArrayUtil.hexStr2Bytes("5B0000007300005A01821200022551250100010000000000B8D3474330373232000000000000000000000000000212010000003D3335303130320000000000373035303400000000000000000000000000000000000000000000000000003F001B3C000000303634383336323338353038A76C5D5B0000007300005A01821200022551250100010000000000B8D3474330373232000000000000000000000000000212010000003D3335303130320000000000373035303400000000000000000000000000000000000000000000000000003F001B3C000000303634383336323338353038A76C5D"));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead..........");
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exceptionCaught..........");
        cause.printStackTrace();
        ctx.close();
    }
}
