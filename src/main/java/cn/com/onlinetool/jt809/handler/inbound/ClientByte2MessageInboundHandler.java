package cn.com.onlinetool.jt809.handler.inbound;

import cn.com.onlinetool.jt809.bean.Message;
import cn.com.onlinetool.jt809.config.BusinessConfig;
import cn.com.onlinetool.jt809.config.NettyConfig;
import cn.com.onlinetool.jt809.decoder.Byte2MessageDecoder;
import cn.com.onlinetool.jt809.handler.CommonHandlerFactory;
import cn.com.onlinetool.jt809.manage.TcpChannelMsgManage;
import cn.com.onlinetool.jt809.util.ByteArrayUtil;
import cn.com.onlinetool.jt809.util.PacketUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author choice
 * 解码处理
 * @date 2019-07-03 09:55
 *
 */
@Slf4j
@Service
@ChannelHandler.Sharable
public class ClientByte2MessageInboundHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    Byte2MessageDecoder decoder;
    @Autowired
    NettyConfig.NettyServerConfig nettyConfig;
    @Autowired
    BusinessConfig businessConfig;
    @Autowired
    TcpChannelMsgManage tcpChannelMsgManage;
    @Autowired
    CommonHandlerFactory handlerFactory;


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //计数器减1
        tcpChannelMsgManage.decrementTcpClientConnectNum();
        log.info("断开服务端连接,服务端信息 : {}",ctx.channel().remoteAddress().toString());
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        decoder.decode(ctx,byteBuf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {

            }else if(state == IdleState.WRITER_IDLE){
                log.warn("发送心跳包到服务端 : {}",ctx.channel().remoteAddress().toString());
                this.sendTestMsgToServer(ctx);
            }else if(state == IdleState.ALL_IDLE){
                log.warn("发送心跳包到服务端 : {}",ctx.channel().remoteAddress().toString());
                this.sendTestMsgToServer(ctx);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("发生错误.......",cause);
        ctx.close();
    }

    /**
     * 测试心跳包发送
     * @param ctx
     * @return
     */
    private void sendTestMsgToServer(ChannelHandlerContext ctx){
        byte[] testLogin = ByteArrayUtil.hexStr2Bytes("5B0000001A0000257A10050001E24001000100000000001C525D");
        Message message = PacketUtil.bytes2Message(testLogin);
        ctx.write(message);
    }
}
