package cn.com.onlinetool.jt809.handler;

import cn.com.onlinetool.jt809.bean.BasePacket;
import cn.com.onlinetool.jt809.util.ByteArrayUtil;
import cn.com.onlinetool.jt809.util.PacketUtil;
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
        int datatLen = ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(basePacket.getMsgBody(),24,4));
        byte[] data = ByteArrayUtil.subBytes(basePacket.getMsgBody(),28,basePacket.getMsgBody().length);

        switch (dataType){
            case UpExgMsg.UP_EXG_MSG_REGISTER:
                upExgRegister(data);
                break;
            case UpExgMsg.UP_EXG_MSG_REAL_LOCATION:
                upExgMsgRealLocation(data);
                break;
            default:
        }
    }


    /**
     * 车辆注册信息处理
     * @param data
     */
    private void upExgRegister(byte[] data){
        int index = 0;
        String platformId = ByteArrayUtil.bytes2string(ByteArrayUtil.subBytes(data,index,11));
        index += 11;
        String producerId = ByteArrayUtil.bytes2string(ByteArrayUtil.subBytes(data,index,11));
        index += 11;
        String terminalModelType = ByteArrayUtil.bytes2string(ByteArrayUtil.subBytes(data,index,8));
        index += 8;
        String terminalId = ByteArrayUtil.bytes2string(ByteArrayUtil.subBytes(data,index,7));
        index += 7;
        String terminalSimCode = ByteArrayUtil.bytes2string(ByteArrayUtil.subBytes(data,index,12));
        index += 12;
    }

    /**
     * 车辆实时定位信息处理
     * @param data
     */
    private void upExgMsgRealLocation(byte[] data){
        int index = 0;
        int encrypy =  ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(data,index,1));
        index += 1;
        String dateTime =  ByteArrayUtil.bytes2HexStr(ByteArrayUtil.subBytes(data,index,7));
        index += 7;
        double lon =  PacketUtil.parseLonOrLat(ByteArrayUtil.subBytes(data,index,4));
        index += 4;
        double lat =  PacketUtil.parseLonOrLat(ByteArrayUtil.subBytes(data,index,4));
        index += 4;
        int vec1 =  ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(data,index,2));
        index += 2;
        int vec2 =  ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(data,index,2));
        index += 2;
        int vec3 =  ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(data,index,4));
        index += 4;
        int direction =  ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(data,index,2));
        index += 2;
        int altitude =  ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(data,index,2));
        index += 2;
        int state =  ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(data,index,2));
        index += 4;
        int alarm =  ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(data,index,2));
        index += 4;

    }

}
