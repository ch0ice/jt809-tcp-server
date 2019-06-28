package cn.com.onlinetool.jt809.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author choice
 * @description:
 * @date 2019-06-28 11:56
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class MessageHead {
    private int msgLength;
    private int msgSn;
    private int msgId;
    private int msgGnssCenterId;
    private byte[] versionFlag;
    private int encrypyFlag;
    private int encrypyKey;
}
