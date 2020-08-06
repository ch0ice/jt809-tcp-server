package cn.com.onlinetool.jt809.handler;

import cn.com.onlinetool.jt809.bean.Message;
import cn.com.onlinetool.jt809.bean.UpExgMsg;
import cn.com.onlinetool.jt809.constants.JT809DataTypeConstants;
import cn.com.onlinetool.jt809.util.ByteArrayUtil;
import cn.com.onlinetool.jt809.util.PacketUtil;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


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
    public void handler(ChannelHandlerContext ctx, Message msg) {
        int index = 0;
        String vehicleNo = ByteArrayUtil.bytes2gbkString(ByteArrayUtil.subBytes(msg.getMsgBody(),index,21));
        vehicleNo = vehicleNo.replaceAll("\\u0000","");
        index += 21;
        int vehicleColor = ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(msg.getMsgBody(),index,1));
        index += 1;
        int dataType = ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(msg.getMsgBody(),index,2));
        index += 2;
        int dataLen = ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(msg.getMsgBody(),index,4));
        index += 4;
        byte[] data = ByteArrayUtil.subBytes(msg.getMsgBody(),index,dataLen);
        index += dataLen;
        UpExgMsg upExgMsg = new UpExgMsg();
        upExgMsg.setVehicleNo(vehicleNo);
        upExgMsg.setVehicleColor(vehicleColor);
        upExgMsg.setDataType(dataType);
        upExgMsg.setDataLen(dataLen);
        upExgMsg.setData(data);
        switch (dataType){
            case JT809DataTypeConstants.UpExgMsg.UP_EXG_MSG_REGISTER:
                upExgRegisterHandler(upExgMsg);
                break;
            case JT809DataTypeConstants.UpExgMsg.UP_EXG_MSG_REAL_LOCATION:
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
        String platformId = ByteArrayUtil.bytes2gbkString(ByteArrayUtil.subBytes(msg.getData(),index,11));
        index += 11;
        String producerId = ByteArrayUtil.bytes2gbkString(ByteArrayUtil.subBytes(msg.getData(),index,11));
        index += 11;
        String terminalModelType = ByteArrayUtil.bytes2gbkString(ByteArrayUtil.subBytes(msg.getData(),index,8));
        index += 8;
        String terminalId = ByteArrayUtil.bytes2gbkString(ByteArrayUtil.subBytes(msg.getData(),index,7));
        index += 7;
        String terminalSimCode = ByteArrayUtil.bytes2gbkString(ByteArrayUtil.subBytes(msg.getData(),index,12));
        index += 12;

        UpExgMsg.UpExgMsgRegister register = new UpExgMsg.UpExgMsgRegister();
        register.setPlatformId(platformId);
        register.setProducerId(producerId);
        register.setTerminalModelType(terminalModelType);
        register.setTerminalId(terminalId);
        register.setTerminalSimCode(terminalSimCode);
        msg.setUpExgMsgRegister(register);
        log.info("车辆动态信息交换-车辆注册信息：" + JSONObject.toJSONString(msg));

    }

    /**
     * 车辆实时定位信息处理
     * @param msg
     */
    private void upExgMsgRealLocationHandler(UpExgMsg msg){
        int index = 0;
        int encrypy =  ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(msg.getData(),index,1));
        index += 1;
        byte[] dateTime =  ByteArrayUtil.subBytes(msg.getData(),index,7);
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
        String state =  ByteArrayUtil.bytes2bitStr(ByteArrayUtil.subBytes(msg.getData(),index,4));
        index += 4;
        String alarm =  ByteArrayUtil.bytes2bitStr(ByteArrayUtil.subBytes(msg.getData(),index,4));
        index += 4;

        UpExgMsg.UpExgMsgRealLocation location = new UpExgMsg.UpExgMsgRealLocation();
        location.setEncrypy(encrypy);
        location.setDateTime(parseDateTime(dateTime));
        location.setLon(lon);
        location.setLat(lat);
        location.setVec1(vec1);
        location.setVec2(vec2);
        location.setVec3(vec3);
        location.setDirection(direction);
        location.setAltitude(altitude);

        UpExgMsg.UpExgMsgRealLocation.State upExgState = new UpExgMsg.UpExgMsgRealLocation.State();
        int stateIdx = 0;
        upExgState.setAcc(Integer.parseInt(state.substring(stateIdx,++stateIdx)));
        upExgState.setLocation(Integer.parseInt(state.substring(stateIdx,++stateIdx)));
        upExgState.setLon(Integer.parseInt(state.substring(stateIdx,++stateIdx)));
        upExgState.setLat(Integer.parseInt(state.substring(stateIdx,++stateIdx)));
        upExgState.setOperation(Integer.parseInt(state.substring(stateIdx,++stateIdx)));
        upExgState.setLatLonEncryption(Integer.parseInt(state.substring(stateIdx,++stateIdx)));
        upExgState.setForwardCollisionWarning(Integer.parseInt(state.substring(stateIdx,++stateIdx)));
        upExgState.setLaneDepartureWarning(Integer.parseInt(state.substring(stateIdx,++stateIdx)));
        upExgState.setLoadRating(Integer.parseInt(state.substring(stateIdx,++stateIdx))*2+Integer.parseInt(state.substring(stateIdx,++stateIdx)));
        upExgState.setOilPath(Integer.parseInt(state.substring(stateIdx,++stateIdx)));
        upExgState.setElectricCircuit(Integer.parseInt(state.substring(stateIdx,++stateIdx)));
        upExgState.setDoor(Integer.parseInt(state.substring(stateIdx,++stateIdx)));
        location.setState(upExgState);

        UpExgMsg.UpExgMsgRealLocation.Alarm upExgAlarm = new UpExgMsg.UpExgMsgRealLocation.Alarm();
        int alarmIdx = 0;
        upExgAlarm.setEmergencyAlarm(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        upExgAlarm.setOverSpeed(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        upExgAlarm.setFatigueDriving(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        upExgAlarm.setEarlyWarning(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        upExgAlarm.setGnssModuleError(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        upExgAlarm.setGnssAntennaDisconnect(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        upExgAlarm.setGnssAntennaShortCircuit(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        upExgAlarm.setTerminalMainPowerUnderVoltage(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        upExgAlarm.setTerminalMainPowerFailure(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        upExgAlarm.setTerminalLcdError(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        upExgAlarm.setTtsModuleError(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        upExgAlarm.setCameraError(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        upExgAlarm.setIcModuleError(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        upExgAlarm.setOverspeedWarning(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        upExgAlarm.setDriverFatigueMonitor(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        upExgAlarm.setBanOnDrivingWarning(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        upExgAlarm.setCumulativeDrivingTimeout(Integer.parseInt(alarm.substring(alarmIdx += 2,++alarmIdx)));
        upExgAlarm.setStopTimeout(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        upExgAlarm.setInOutArea(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        upExgAlarm.setInOutRoute(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        upExgAlarm.setRoadDrivingTimeout(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        upExgAlarm.setLaneDepartureError(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        upExgAlarm.setVssError(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        upExgAlarm.setOilError(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        upExgAlarm.setStolen(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        upExgAlarm.setIllegalIgnition(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        upExgAlarm.setIllegalMove(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        upExgAlarm.setCollisionRollover(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        upExgAlarm.setRolloverWarning(Integer.parseInt(alarm.substring(alarmIdx,++alarmIdx)));
        location.setAlarm(upExgAlarm);

        msg.setUpExgMsgRealLocation(location);
        log.info("车辆动态信息交换-车辆实时定位信息：" + JSONObject.toJSONString(msg));
    }

    private String parseDateTime(byte[] dateTime){
        int year = ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(dateTime,2,2));
        int month = ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(dateTime,1,1));
        int day = ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(dateTime,0,1));
        int hour = ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(dateTime,4,1));
        int minute = ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(dateTime,5,1));
        int second = ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(dateTime,6,1));
        return "" + year +
                (month < 10 ? "0" + month : month) +
                (day < 10 ? "0" + day : day) +
                (hour < 10 ? "0" + hour : hour) +
                (minute < 10 ? "0" + minute : minute) +
                (second < 10 ? "0" + second : second);
    }

}
