package cn.com.onlinetool.jt809.handler;

import cn.com.onlinetool.jt809.bean.Message;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author choice
 * 从链路链接保持
 * @date 2019-07-19 14:52
 *
 */
@Slf4j
@Component
public class DownLinkTestHandler implements CommonHandler{

    @Override
    public void handler(ChannelHandlerContext ctx, Message msg) {

    }
}