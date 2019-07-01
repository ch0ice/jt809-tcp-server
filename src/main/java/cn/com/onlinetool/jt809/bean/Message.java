package cn.com.onlinetool.jt809.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author choice
 * @date 2019-06-28 11:55
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Message {
    private int headFlag;
    private MessageHead msgHead;
    private byte[] msgBody;
    private byte[] crcCode;
    private int endFlag;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class MessageHead {
        private int msgLength;
        private int msgSn;
        private short msgId;
        private int msgGnssCenterId;
        private byte[] versionFlag;
        private int encryptFlag;
        private int encryptKey;
    }
}
