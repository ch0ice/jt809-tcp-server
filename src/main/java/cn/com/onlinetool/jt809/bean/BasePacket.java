package cn.com.onlinetool.jt809.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author choice
 * @description:
 * @date 2019-06-28 11:27
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class BasePacket {

    private byte headFlag;
    private byte[] msgHead;
    private byte[] msgBody;
    private byte[] crcCode;
    private byte endFlag;

}
