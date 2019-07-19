package cn.com.onlinetool.jt809.handler;


import cn.com.onlinetool.jt809.constants.JT809DataTypeConstants;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author choice
 * @description: 初始化逻辑处理类
 * @date 2018-12-27 12:27
 *
 */
@Component
public class CommonHandlerFactory implements ApplicationContextAware {
    private static Map<Integer, CommonHandler> handlers = new ConcurrentHashMap<>();
    private static ApplicationContext context;


    @PostConstruct
    public void serverHandlerFactory() {
        handlers.put(JT809DataTypeConstants.UP_CONNECT_REQ,context.getBean(UpConnectHandler.class));
        handlers.put(JT809DataTypeConstants.UP_EXG_MSG,context.getBean(UpExgMsgHandler.class));
        handlers.put(JT809DataTypeConstants.UP_LINKTEST_REQ,context.getBean(UpLinkTestHandler.class));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(null == context){
            context = applicationContext;
        }
    }

    public CommonHandler getHandler(Integer flag){
        return handlers.get(flag);
    }


}
