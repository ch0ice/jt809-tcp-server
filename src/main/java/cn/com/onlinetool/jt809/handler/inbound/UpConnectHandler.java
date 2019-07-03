package cn.com.onlinetool.jt809.handler.inbound;

import cn.com.onlinetool.jt809.bean.Message;
import cn.com.onlinetool.jt809.bean.UpConnectReq;
import cn.com.onlinetool.jt809.constants.JT809ResCodeConstants;
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
        this.login(ctx,msg);

    }

    /**
     * 登陆逻辑处理
     * @param msg
     */
    private void login(ChannelHandlerContext ctx,Message msg){
        int index = 0;
        int userId = ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(msg.getMsgBody(),index,4));
        index += 4;
        String password = ByteArrayUtil.bytes2gbkString(ByteArrayUtil.subBytes(msg.getMsgBody(),index,8));
        index += 8;
        String downLinkIp = ByteArrayUtil.bytes2gbkString(ByteArrayUtil.subBytes(msg.getMsgBody(),index,32));
        index += 32;
        String downLinkPort = ByteArrayUtil.bytes2gbkString(ByteArrayUtil.subBytes(msg.getMsgBody(),index,2));
        index += 2;

        UpConnectReq req = new UpConnectReq();
        req.setUserId(userId);
        req.setPassword(password);
        req.setDownLinkIp(downLinkIp);
        req.setDownLinkPort(downLinkPort);
        log.info("登陆请求信息：" + JSONObject.toJSONString(req));

        int testUser = 123456;
        String testPass = "jt809test";
        if(testUser == req.getUserId() && testPass.equals(req.getPassword())){
            log.info("登陆成功");
            byte[] result = new byte[]{JT809ResCodeConstants.UpConnect.SUCCESS};
            byte[] verifyCode = new byte[4];
            ByteArrayUtil.append(result,verifyCode);
            msg.setMsgBody(result);
            ctx.writeAndFlush(msg);
        }
        else if("".equals(req.getDownLinkIp())){
            log.info("ip地址不正确");
            byte[] result = new byte[]{JT809ResCodeConstants.UpConnect.IP_ERROR};
            byte[] verifyCode = new byte[4];
            ByteArrayUtil.append(result,verifyCode);
            msg.setMsgBody(result);
            ctx.writeAndFlush(msg);
        }
        else if(111 != msg.getMsgHead().getMsgGnssCenterId()){
            log.info("接入码不正确");
            byte[] result = new byte[]{JT809ResCodeConstants.UpConnect.GUSSCENTERID_ERROR};
            byte[] verifyCode = new byte[4];
            ByteArrayUtil.append(result,verifyCode);
            msg.setMsgBody(result);
            ctx.writeAndFlush(msg);
        }
        else if(testUser != req.getUserId()){
            log.info("用户没有注册");
            byte[] result = new byte[]{JT809ResCodeConstants.UpConnect.USER_NOT_EXIST};
            byte[] verifyCode = new byte[4];
            ByteArrayUtil.append(result,verifyCode);
            msg.setMsgBody(result);
            ctx.writeAndFlush(msg);
        }
        else if(testPass.equals(req.getPassword())){
            log.info("密码错误");
            byte[] result = new byte[]{JT809ResCodeConstants.UpConnect.PASSWORD_ERROR};
            byte[] verifyCode = new byte[4];
            ByteArrayUtil.append(result,verifyCode);
            msg.setMsgBody(result);
            ctx.writeAndFlush(msg);
        }
        else {
            log.info("其他错误");
            byte[] result = new byte[]{JT809ResCodeConstants.UpConnect.OTHER_ERROR};
            byte[] verifyCode = new byte[4];
            ByteArrayUtil.append(result,verifyCode);
            msg.setMsgBody(result);
            ctx.writeAndFlush(msg);
        }
    }

}
