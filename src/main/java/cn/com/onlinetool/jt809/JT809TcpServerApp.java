package cn.com.onlinetool.jt809;


import cn.com.onlinetool.jt809.server.JT809Server;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.web.client.RestTemplate;

/**
 * @author choice
 */
@SpringBootApplication
//@EnableKafka
public class JT809TcpServerApp {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    public static void main(String[] args) throws Exception {
        ApplicationContext context = new SpringApplicationBuilder(JT809TcpServerApp.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
        JT809Server JT809Server = context.getBean(JT809Server.class);
        JT809Server.runServer();
    }

}
