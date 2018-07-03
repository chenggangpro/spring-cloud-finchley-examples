package pro.chenggang.example.spring.cloud.actuator;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * //TODO[特别注意] 启动注解为@SpringBootApplication否则无法向Eureka注册
 * 官网使用注解 @Configuration和@EnableAutoConfiguration
 * 在配置时无法注册Eureka服务,使用@SpringBootApplication注解后正常
 */
@SpringBootApplication
@EnableEurekaClient
@EnableAdminServer
public class SpringCloudFinchleyActuatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudFinchleyActuatorApplication.class, args);
	}
}
