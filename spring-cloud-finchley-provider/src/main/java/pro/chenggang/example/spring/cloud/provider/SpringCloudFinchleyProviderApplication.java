package pro.chenggang.example.spring.cloud.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SpringCloudFinchleyProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudFinchleyProviderApplication.class, args);
	}
}
