package cn.com.onlinetool.jt809.handler.inbound;

import cn.com.onlinetool.jt809.bean.Message;
import cn.com.onlinetool.jt809.bean.UpConnectReq;
import cn.com.onlinetool.jt809.util.ByteArrayUtil;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author choice
 * 主链路登陆逻辑处理
 * @date 2019-06-28 14:35
 *
 */
@Slf4j
@Component
public class UpConnectHandler implements CommonHandler {
//    @Autowired
//    KafkaTemplate<String,Object> kafkaTemplate;
//    @Autowired
//    TcpChannelMsgManage tcpChannelMsgManage;

    @Override
    public void handler(ChannelHandlerContext ctx, Message msg) {
        int userId = ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(msg.getMsgBody(),0,4));
        String password = ByteArrayUtil.bytes2gbkString(ByteArrayUtil.subBytes(msg.getMsgBody(),4,8));
        String downLinkIp = ByteArrayUtil.bytes2gbkString(ByteArrayUtil.subBytes(msg.getMsgBody(),12,32));
        String downLinkPort = ByteArrayUtil.bytes2gbkString(ByteArrayUtil.subBytes(msg.getMsgBody(),44,2));

        UpConnectReq req = new UpConnectReq();
        req.setUserId(userId);
        req.setPassword(password);
        req.setDownLinkIp(downLinkIp);
        req.setDownLinkPort(downLinkPort);
        this.login(req);
        this.loginRes(ctx,msg);

    }

    /**
     * 登陆逻辑处理
     * @param msg
     */
    private void login(UpConnectReq msg){
        log.info("登陆请求信息：" + JSONObject.toJSONString(msg));
        if(123456 == msg.getUserId() && "jt809test".equals(msg.getPassword())){
            log.info("登陆成功");
        }
    }

    /**
     * 登陆响应处理
     * @param ctx
     * @param msg
     */
    private void loginRes(ChannelHandlerContext ctx, Message msg){
        //测试响应
        ctx.writeAndFlush(msg);
    }
}
