package cn.com.onlinetool.jt809.handler;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author choice
 * @description: 逻辑处理
 * @date 2019-01-01 21:38
 *
 */
public interface CommonHandler {

    void handler(ChannelHandlerContext ctx,Object msg);
}
