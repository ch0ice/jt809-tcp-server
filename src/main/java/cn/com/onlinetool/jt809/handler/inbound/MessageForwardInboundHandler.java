package cn.com.onlinetool.jt809.handler.inbound;

import cn.com.onlinetool.jt809.bean.Message;
import cn.com.onlinetool.jt809.config.BusinessConfig;
import cn.com.onlinetool.jt809.handler.CommonHandlerFactory;
import cn.com.onlinetool.jt809.util.ByteArrayUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author choice
 * 将解码后的Message对象转发各个消息类型的handler
 * @date 2019-07-03 10:26
 *
 */
@Slf4j
@Service
@ChannelHandler.Sharable
public class MessageForwardInboundHandler extends ChannelInboundHandlerAdapter {
    @Autowired
    BusinessConfig businessConfig;
    @Autowired
    CommonHandlerFactory handlerFactory;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message)msg;
        if(null == handlerFactory.getHandler((int)message.getMsgHead().getMsgId())){
            log.warn("暂不支持此类型消息：" + ByteArrayUtil.short2HexStr(message.getMsgHead().getMsgId()));
            return;
        }
        handlerFactory.getHandler((int)message.getMsgHead().getMsgId()).handler(ctx,message);
}

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("数据读取完成");
        super.channelReadComplete(ctx);
    }
}
