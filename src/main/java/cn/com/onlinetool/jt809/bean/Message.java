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
    private byte headFlag;
    private MessageHead msgHead;
    private byte[] msgBody;
    private byte[] crcCode;
    private byte endFlag;
}
