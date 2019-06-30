package cn.com.onlinetool.jt809.adapter;

import cn.com.onlinetool.jt809.bean.Message;
import cn.com.onlinetool.jt809.config.BusinessConfig;
import cn.com.onlinetool.jt809.config.NettyConfig;
import cn.com.onlinetool.jt809.handler.CommonHandlerFactory;
import cn.com.onlinetool.jt809.manage.TcpChannelMsgManage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * @author choice
 * @description: 输入数据处理适配器
 * @date 2018-12-27 12:27
 *
 */
@Service
@ChannelHandler.Sharable
public class JT809ServerAdapter extends ChannelInboundHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(JT809ServerAdapter.class);

    @Autowired
    NettyConfig nettyConfig;
    @Autowired
    BusinessConfig businessConfig;
    @Autowired
    TcpChannelMsgManage tcpChannelMsgManage;
    @Autowired
    CommonHandlerFactory handlerFactory;

    private static ExecutorService businessExecutor;
    @PostConstruct
    public void businessExecutorInit() {
        businessExecutor = new ThreadPoolExecutor(businessConfig.getCorePoolSize(), businessConfig.getMaximumPoolSize(), businessConfig.getKeepAliveTime(), TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(100000));
    }


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
        if(null != nettyConfig.getMaxConnectNum() && nettyConfig.getMaxConnectNum() > 0){
            //判断当前连接数是否大于 配置的最大连接数 如果大于最大连接数直接关闭连接
            if(tcpChannelMsgManage.getTcpClientConnectNum() > nettyConfig.getMaxConnectNum()){
                logger.warn("当前节点连接数已经超过最大连接数 : {}", tcpChannelMsgManage.getTcpClientConnectNum());
                //关闭客户端连接
                ctx.channel().close();
                return;
            }
        }

        //计数器加1
        tcpChannelMsgManage.incrementTcpClientConnectNum();
        logger.info("连接到新客户端，当前节点连接数 : {}", tcpChannelMsgManage.getTcpClientConnectNum());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        //计数器减1
        tcpChannelMsgManage.decrementTcpClientConnectNum();
        logger.info("断开客户端连接,客户端信息 : {}",ctx.channel().remoteAddress().toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        businessExecutor.submit(() -> {
            Message message = (Message) msg;
            handlerFactory.getHandler(message.getMsgHead().getMsgId()).handler(ctx,message);
        });
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
                logger.warn("客户端连接超时,客户端信息 : {}",ctx.channel().remoteAddress().toString());
                ctx.close();
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
        logger.error("发生错误.......",cause);
        ctx.close();
    }

}
