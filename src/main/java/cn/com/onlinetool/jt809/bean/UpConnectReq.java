package cn.com.onlinetool.jt809.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author choice
 * 主链路登陆请求
 * @date 2019-06-28 15:37
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpConnectReq {
    private int userId;
    private String password;
    private String downLinkIp;
    private int downLinkPort;
}
