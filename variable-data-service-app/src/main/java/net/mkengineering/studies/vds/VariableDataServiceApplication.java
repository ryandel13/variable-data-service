package net.mkengineering.studies.vds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class VariableDataServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VariableDataServiceApplication.class, args);
	}
}
