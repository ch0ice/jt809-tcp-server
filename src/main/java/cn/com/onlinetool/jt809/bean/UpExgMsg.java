package cn.com.onlinetool.jt809.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author choice
 * 车辆动态信息交换
 * @date 2019-06-28 17:04
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpExgMsg {
    private String vehicleNo;
    private int vehicleColor;
    private int dataType;
    private int dataLen;
    private byte[] data;
    private UpExgMsgRegister upExgMsgRegister;
    private UpExgMsgRealLocation upExgMsgRealLocation;

    /**
     * 车辆注册信息
     */
    @Data
    @NoArgsConstructor
    public static class UpExgMsgRegister{
        private String platformId;
        private String producerId;
        private String terminalModelType;
        private String terminalId;
        private String terminalSimCode;
    }

    /**
     * 车辆实时定位信息
     */
    @Data
    @NoArgsConstructor
    public static class UpExgMsgRealLocation{
        private int encrypy;
        private String dateTime;
        private double lon;
        private double lat;
        private int vec1;
        private int vec2;
        private int vec3;
        private int direction;
        private int altitude;
        private int state;
        private int alarm;
    }
}
