package cn.com.onlinetool.jt809.constants;

/**
 * @author choice
 * @description:
 * @date 2019-06-28 13:44
 *
 */
public final class JT809MessageConstants {
    public static final int MSG_HEAD = 0x5b;
    public static final int MSG_TAIL = 0x5d;
    public static final int MSG_HEAD_LEN = 22;
    public static final int MSG_MIN_LEN = 1 + 22 + 2 + 1;
    public static final int MSG_CRC_LEN = 2;
}
