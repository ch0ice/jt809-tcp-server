package cn.com.onlinetool.jt809.constants;

/**
 * @author choice
 * JT809 业务数据类型
 * @date 2019-06-28 14:45
 *
 */
public final class JT809DataTypeConstants {
    /**
     * 主链路登录请求消息
     */
    public final static int UP_CONNECT_REQ = 0x1001;
    /**
     * 主链路登录应答消息
     */
    public final static int UP_CONNECT_RSP = 0x1002;
    /**
     * 主链路注销请求消息
     */
    public final static int UP_DICONNECE_REQ = 0x1003;
    /**
     * 主链路注销应答消息
     */
    public final static int UP_DISCONNECT_RSP = 0x1004;
    /**
     * 主链路连接保持请求消息
     */
    public final static int UP_LINKETEST_REQ = 0x1005;
    /**
     * 主链路连接保持应答消息
     */
    public final static int UP_LINKTEST_RSP = 0x1006;
    /**
     * 主链路动态信息交换消息
     */
    public final static int UP_EXG_MSG = 0x1200;
    public final static class UpExg {
        /**
         * 上传车辆注册信息
         */
        public final static int UP_EXG_MSG_REGISTER = 0x1201;
        /**
         * 实时上传车辆定位信息
         */
        public final static int UP_EXG_MSG_REAL_LOCATION = 0x1202;
        /**
         * 车辆定位信息自动补报
         */
        public final static int UP_EXG_MSG_HISTORY_LOCATION = 0x1203;
        /**
         * 启动车辆定位信息交换应答
         */
        public final static int UP_EXG_MSG_RETURE_STARTUP_ACK = 0x1205;
        /**
         * 结束车辆定位信息交换应答
         */
        public final static int UP_EXG_MSG_RETURE_END_ACK = 0x1206;
        /**
         * 申请交换指定车辆定位信息请求
         */
        public final static int UP_EXG_MSG_APPLE_FOR_MONITOR_STAR_TUP = 0x1207;
        /**
         * 取消交换制定车辆定位信息请求
         */
        public final static int UP_EXG_MSG_APPLE_FOR_MONITOR_END = 0x1208;
        /**
         * 补发车辆定位信息请求
         */
        public final static int UP_EXG_MSG_APPLE_HISGNSSDATA_REQ = 0x1209;
        /**
         * 上报车辆驾驶员身份识别信息应答
         */
        public final static int UP_EXG_MSG_REPORT_DRIVER_INFO_ACK = 0x120A;
        /**
         * 上报车辆电子运单应答
         */
        public final static int UP_EXG_MSG_TAKE_EWAYBILL_ACK = 0x120B;

    }




}
