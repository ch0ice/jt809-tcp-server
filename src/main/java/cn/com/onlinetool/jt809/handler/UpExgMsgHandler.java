package cn.com.onlinetool.jt809.handler;

import cn.com.onlinetool.jt809.bean.BasePacket;
import cn.com.onlinetool.jt809.bean.UpExgMsg;
import cn.com.onlinetool.jt809.util.ByteArrayUtil;
import cn.com.onlinetool.jt809.util.PacketUtil;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static cn.com.onlinetool.jt809.constants.JT809DataTypeConstants.*;

/**
 * @author choice
 * 车辆动态信息交换逻辑处理
 * @date 2019-06-28 15:49
 *
 */
@Slf4j
@Component
public class UpExgMsgHandler implements CommonHandler{

    @Override
    public void handler(ChannelHandlerContext ctx, Object msg) {
        BasePacket basePacket = (BasePacket) msg;
        String vehicleNo = ByteArrayUtil.bytes2string(ByteArrayUtil.subBytes(basePacket.getMsgBody(),0,21));
        int vehicleColor = ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(basePacket.getMsgBody(),21,1));
        int dataType = ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(basePacket.getMsgBody(),22,2));
        int dataLen = ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(basePacket.getMsgBody(),24,4));
        byte[] data = ByteArrayUtil.subBytes(basePacket.getMsgBody(),28,basePacket.getMsgBody().length);
        UpExgMsg upExgMsg = new UpExgMsg();
        upExgMsg.setVehicleNo(vehicleNo);
        upExgMsg.setVehicleColor(vehicleColor);
        upExgMsg.setDataType(dataType);
        upExgMsg.setDataLen(dataLen);
        upExgMsg.setData(data);
        switch (dataType){
            case UpExgConstants.UP_EXG_MSG_REGISTER:
                upExgRegisterHandler(upExgMsg);
                break;
            case UpExgConstants.UP_EXG_MSG_REAL_LOCATION:
                upExgMsgRealLocationHandler(upExgMsg);
                break;
            default:
        }
    }


    /**
     * 车辆注册信息处理
     * @param msg
     */
    private void upExgRegisterHandler(UpExgMsg msg){
        int index = 0;
        String platformId = ByteArrayUtil.bytes2string(ByteArrayUtil.subBytes(msg.getData(),index,11));
        index += 11;
        String producerId = ByteArrayUtil.bytes2string(ByteArrayUtil.subBytes(msg.getData(),index,11));
        index += 11;
        String terminalModelType = ByteArrayUtil.bytes2string(ByteArrayUtil.subBytes(msg.getData(),index,8));
        index += 8;
        String terminalId = ByteArrayUtil.bytes2string(ByteArrayUtil.subBytes(msg.getData(),index,7));
        index += 7;
        String terminalSimCode = ByteArrayUtil.bytes2string(ByteArrayUtil.subBytes(msg.getData(),index,12));
        index += 12;

        UpExgMsg.UpExgMsgRegister register = new UpExgMsg.UpExgMsgRegister();
        register.setPlatformId(platformId);
        register.setProducerId(producerId);
        register.setTerminalModelType(terminalModelType);
        register.setTerminalId(terminalId);
        register.setTerminalSimCode(terminalSimCode);
        msg.setUpExgMsgRegister(register);
        log.info("车辆注册信息：" + JSONObject.toJSONString(register));

    }

    /**
     * 车辆实时定位信息处理
     * @param msg
     */
    private void upExgMsgRealLocationHandler(UpExgMsg msg){
        int index = 0;
        int encrypy =  ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(msg.getData(),index,1));
        index += 1;
        String dateTime =  ByteArrayUtil.bytes2HexStr(ByteArrayUtil.subBytes(msg.getData(),index,7));
        index += 7;
        double lon =  PacketUtil.parseLonOrLat(ByteArrayUtil.subBytes(msg.getData(),index,4));
        index += 4;
        double lat =  PacketUtil.parseLonOrLat(ByteArrayUtil.subBytes(msg.getData(),index,4));
        index += 4;
        int vec1 =  ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(msg.getData(),index,2));
        index += 2;
        int vec2 =  ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(msg.getData(),index,2));
        index += 2;
        int vec3 =  ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(msg.getData(),index,4));
        index += 4;
        int direction =  ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(msg.getData(),index,2));
        index += 2;
        int altitude =  ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(msg.getData(),index,2));
        index += 2;
        int state =  ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(msg.getData(),index,2));
        index += 4;
        int alarm =  ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(msg.getData(),index,2));
        index += 4;

        UpExgMsg.UpExgMsgRealLocation location = new UpExgMsg.UpExgMsgRealLocation();
        location.setEncrypy(encrypy);
        location.setDateTime(dateTime);
        location.setLon(lon);
        location.setLat(lat);
        location.setVec1(vec1);
        location.setVec2(vec2);
        location.setVec3(vec3);
        location.setDirection(direction);
        location.setAltitude(altitude);
        location.setState(state);
        location.setAlarm(alarm);
        msg.setUpExgMsgRealLocation(location);
        log.info("车辆实时定位信息：" + JSONObject.toJSONString(location));


    }

}
