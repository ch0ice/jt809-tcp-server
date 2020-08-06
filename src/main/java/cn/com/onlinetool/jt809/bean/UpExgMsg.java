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
    /**
     * 车牌号
     */
    private String vehicleNo;
    /**
     * 车牌颜色，按照JT/T 415-2006中5.4.12的规定。
     */
    private int vehicleColor;
    /**
     * 子业务类型标识
     */
    private int dataType;
    /**
     * 后续数据长度
     */
    private int dataLen;
    /**
     * 数据部分
     */
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
        /**
         * 制造商 ID
         */
        private String producerId;
        /**
         * 终端型号
         */
        private String terminalModelType;
        /**
         * 终端 ID
         */
        private String terminalId;
        private String terminalSimCode;
    }

    /**
     * 车辆实时定位信息
     */
    @Data
    @NoArgsConstructor
    public static class UpExgMsgRealLocation{
        /**
         * 该字段标识传输的定位信息是否使用国家测绘局批准的地图保密插件进行加密。
         * 加密标识：1-已加密，0-未加密。
         */
        private int encrypy;
        /**
         * 日月年（dmyy），年的表示是先将年转换成2为十六进制数，如2009标识为0x070 xD9.
         * 时分秒（hms）
         */
        private String dateTime;
        /**
         * 经度，单位为1*10-6度。
         */
        private double lon;
        /**
         * 纬度，单位为1*10-6度。
         */
        private double lat;
        /**
         * 速度，指卫星定位车载终端设备上传的行车速度信息，为必填项。单位为千米每小时（km/h）。
         */
        private int vec1;
        /**
         * 行驶记录速度，指车辆行驶记录设备上传的行车速度信息，为必填项。单位为千米每小时（km/h）。
         */
        private int vec2;
        /**
         * 车辆当前总里程数，值车辆上传的行车里程数。单位单位为千米（km）。
         */
        private int vec3;
        /**
         * 方向，0-359，单位为度（。），正北为0，顺时针。
         */
        private int direction;
        /**
         * 限速值，单位为千米每小时（km/h）。注：原ALTITUDE
         */
        private int altitude;
        /**
         * 车辆状态，二进制表示，B31B30B29。。。。。。B2B1B0.具体定义按照JT/T808-2011中表17的规定
         */
        private State state;
        /**
         * 报警状态，二进制表示，0标识正常，1表示报警：
         * B31B30B29。。。。。。B2B1B0.具体定义按照JT/T808-2011中表18的规定
         */
        private Alarm alarm;

        /**
         * 状态信息 参见JT/T808-2011 表17
         */
        @Data
        @NoArgsConstructor
        public static class State{
            /**
             * 0：ACC 关；1： ACC 开
             */
            private int acc;
            /**
             * 0：未定位；1：定位
             */
            private int location;
            /**
             * 0：北纬；1：南纬
             */
            private int lat;
            /**
             * 0：东经；1：西经
             */
            private int lon;
            /**
             * 0：运营状态；1：停运状态
             */
            private int operation;
            /**
             * 0：经纬度未经保密插件加密；1：经纬度已经保密插件加密
             */
            private int latLonEncryption;
            /**
             * 1:紧急刹车系统采集的前撞预警
             */
            private int forwardCollisionWarning;
            /**
             * 1: 车道偏移预警
             */
            private int laneDepartureWarning;
            /**
             * 00：空车；01：半载；10：保留；11：满载
             * （可用于客车的空、重车及货车的空载、满载状态表示，人工输入或传感器
             * 获取）
             */
            private int loadRating;
            /**
             * 0：车辆油路正常；1：车辆油路断开
             */
            private int oilPath;
            /**
             * 0：车辆电路正常；1：车辆电路断开
             */
            private int electricCircuit;
            /**
             * 0：车门解锁；1：车门加锁
             */
            private int door;

        }

        /**
         * 报警信息 参见JT/T808-2011 表18
         */
        @Data
        @NoArgsConstructor
        public static class Alarm{
            /**
             * 1：紧急报警，触动报警开关后触发
             * 收到应答后清零
             */
            private int emergencyAlarm;
            /**
             * 1：超速报警
             * 标志维持至报警条件解除
             */
            private int overSpeed;
            /**
             * 1：疲劳驾驶
             * 标志维持至报警条件解除
             */
            private int fatigueDriving;
            /**
             * 1：预警
             * 收到应答后清零
             */
            private int earlyWarning;
            /**
             * 1：GNSS 模块发生故障
             * 标志维持至报警条件解除
             */
            private int gnssModuleError;
            /**
             * 1：GNSS 天线未接或被剪断
             * 标志维持至报警条件解除
             */
            private int gnssAntennaDisconnect;
            /**
             * 1：GNSS 天线短路
             * 标志维持至报警条件解除
             */
            private int gnssAntennaShortCircuit;
            /**
             * 1：终端主电源欠压 标志维持至报警条件解除
             */
            private int terminalMainPowerUnderVoltage;
            /**
             * 1：终端主电源掉电 标志维持至报警条件解除
             */
            private int terminalMainPowerFailure;
            /**
             * 1：终端 LCD 或显示器故障 标志维持至报警条件解除
             */
            private int terminalLcdError;
            /**
             * 1：TTS 模块故障 标志维持至报警条件解除
             */
            private int ttsModuleError;
            /**
             * 1：摄像头故障 标志维持至报警条件解除
             */
            private int cameraError;
            /**
             * 12 1：道路运输证 IC 卡模块故障 标志维持至报警条件解除
             */
            private int icModuleError;
            /**
             * 13 1：超速预警 标志维持至报警条件解除
             */
            private int overspeedWarning;
            /**
             * 14 1：疲劳驾驶预警 标志维持至报警条件解除
             */
            private int driverFatigueMonitor ;
            /**
             * 15 1：禁止行驶报警 标志维持至报警条件解除
             */
            private int banOnDrivingWarning;
            /**
             * 16～17 保留
             */


            /**
             * 1：当天累计驾驶超时 标志维持至报警条件解除
             */
            private int cumulativeDrivingTimeout;
            /**
             * 1：超时停车 标志维持至报警条件解除
             */
            private int stopTimeout;
            /**
             * 1：进出区域 收到应答后清零
             */
            private int inOutArea;
            /**
             * 1：进出路线 收到应答后清零
             */
            private int inOutRoute;
            /**
             * 1：路段行驶时间不足/过长 收到应答后清零
             */
            private int roadDrivingTimeout;
            /**
             * 1：路线偏离报警 标志维持至报警条件解除
             */
            private int laneDepartureError;
            /**
             * 1：车辆 VSS 故障 标志维持至报警条件解除
             */
            private int vssError;
            /**
             * 1：车辆油量异常 标志维持至报警条件解除
             */
            private int oilError;
            /**
             * 1：车辆被盗(通过车辆防盗器) 标志维持至报警条件解除
             */
            private int stolen;
            /**
             * 1：车辆非法点火 收到应答后清零
             */
            private int illegalIgnition;
            /**
             * 1：车辆非法位移 收到应答后清零
             */
            private int illegalMove;
            /**
             * 29 1：碰撞侧翻报警 标志维持至报警条件解除
             */
            private int collisionRollover;
            /**
             * 30 1：侧翻预警 标志维持至报警条件解除
             */
            private int rolloverWarning;

        }
    }
}
