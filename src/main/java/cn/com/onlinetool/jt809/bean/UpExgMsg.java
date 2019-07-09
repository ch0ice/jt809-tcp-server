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
        private State state;
        private Alarm alarm;

        /**
         * 状态信息 参见JT/T808-2011 表17
         */
        @Data
        @NoArgsConstructor
        public static class State{
            private int acc;
            private int location;
            private int lat;
            private int lon;
            private int operation;
            private int latLonEncryption;
            private int oilPath;
            private int electricCircuit;
            private int door;

        }

        /**
         * 报警信息 参见JT/T808-2011 表18
         */
        @Data
        @NoArgsConstructor
        public static class Alarm{
            private int emergencyAlarm;
            private int overSpeed;
            private int fatigueDriving;
            private int earlyWarning;
            private int gnssModuleError;
            private int gnssAntennaDisconnect;
            private int gnssAntennaShortCircuit;
            private int terminalMainPowerUnderVoltage;
            private int terminalMainPowerFailure;
            private int terminalLcdError;
            private int ttsModuleError;
            private int cameraError;
            private int cumulativeDrivingTimeout;
            private int stopTimeout;
            private int inOutArea;
            private int inOutRoute;
            private int roadDrivingTimeout;
            private int laneDepartureError;
            private int vssError;
            private int oilError;
            private int stolen;
            private int illegalIgnition;
            private int illegalMove;

        }
    }
}
