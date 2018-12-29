package pro.chenggang.example.spring.cloud.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.task.configuration.EnableTask;

@EnableTask
@EnableEurekaClient
@SpringBootApplication
public class SpirngCloudFinchleyTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpirngCloudFinchleyTaskApplication.class, args);
	}

}

