package cn.com.onlinetool.jt809.handler.inbound;

import cn.com.onlinetool.jt809.bean.Message;
import cn.com.onlinetool.jt809.config.BusinessConfig;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    private static ExecutorService businessExecutor;
    @PostConstruct
    public void businessExecutorInit() {
        businessExecutor = new ThreadPoolExecutor(businessConfig.getCorePoolSize(), businessConfig.getMaximumPoolSize(), businessConfig.getKeepAliveTime(), TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(100000));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message)msg;
        businessExecutor.submit(() -> {
            handlerFactory.getHandler((int)message.getMsgHead().getMsgId()).handler(ctx,message);
        });
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("数据读取完成");
        super.channelReadComplete(ctx);
    }
}
