package cn.com.onlinetool.jt809.enums;

/**
 * @author choice
 * @description: 保存协议中定长部分起始下表和长度
 * @date 2018-12-28 10:50
 *
 */
public enum BasePacketEnum {
    //起始符
    HEAD(0,2),
    //命令单元 请求标志
    COMMAND_UNIT_REQ_FLAG(2,1),
    //命令单元 应答标志
    COMMAND_UNIT_RES_FLAG(3,1),
    //唯一识别码
    ONLINE_CODE(4,17),
    //数据单元加密类型
    DATA_UNIT_ENCRYPT_TYPE(21,1),
    //数据单元长度
    DATA_UNIT_LENGHT(22,2);

    private int beginIdx;
    private int lenght;
    BasePacketEnum(int beginIdx, int length) {
        this.beginIdx = beginIdx;
        this.lenght = length;
    }

    public int getBeginIdx() {
        return beginIdx;
    }

    public int getLenght() {
        return lenght;
    }


    /**
     * 上报数据的最短长度
     */
    public static final int DATA_UNIT_MIN_LENGHT = 25;
    /**
     * 国标数据请求头
     */
    public static final byte[] BEGIN_SYMBO = {0x23,0x23};

}
