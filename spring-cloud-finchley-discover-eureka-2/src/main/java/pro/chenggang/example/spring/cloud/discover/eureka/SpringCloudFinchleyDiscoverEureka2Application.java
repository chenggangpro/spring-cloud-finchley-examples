package pro.chenggang.example.spring.cloud.discover.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SpringCloudFinchleyDiscoverEureka2Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudFinchleyDiscoverEureka2Application.class, args);
	}
}
