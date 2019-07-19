package cn.com.onlinetool.jt809.handler;

import cn.com.onlinetool.jt809.bean.Message;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author choice
 * 主链路链接保持处理
 * @date 2019-06-28 15:49
 *
 */
@Slf4j
@Component
public class UpLinkTestHandler implements CommonHandler{

    @Override
    public void handler(ChannelHandlerContext ctx, Message msg) {
        log.info("接收到心跳包");
        ctx.write(msg);
    }
}