package cn.com.onlinetool.jt809.handler.outbound;

import cn.com.onlinetool.jt809.bean.Message;
import cn.com.onlinetool.jt809.encoder.Message2ByteEncoder;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.SocketAddress;

/**
 * @author choice
 * @description: 输出数据处理适配器
 * @date 2018-12-27 12:27
 *
 */
@Slf4j
@Service
@ChannelHandler.Sharable
public class Message2ByteOutboundHandler extends ChannelOutboundHandlerAdapter {

    @Autowired
    Message2ByteEncoder message2ByteEncoder;


    @Override
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        System.out.println("bind..........");
        super.bind(ctx,localAddress,promise);
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        System.out.println("connect..........");
        super.connect(ctx,remoteAddress,localAddress,promise);
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        System.out.println("disconnect..........");
        super.disconnect(ctx,promise);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        System.out.println("close..........");
        super.close(ctx,promise);
    }

    @Override
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        System.out.println("deregister..........");
        super.deregister(ctx,promise);
    }

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        System.out.println("read..........");
        super.read(ctx);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("write..........");
        message2ByteEncoder.encode(ctx,(Message) msg);
        ctx.flush();
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        System.out.println("flush..........");
        super.flush(ctx);
    }
}
