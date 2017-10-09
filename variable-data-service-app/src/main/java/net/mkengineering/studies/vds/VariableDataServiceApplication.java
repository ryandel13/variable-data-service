package net.mkengineering.studies.vds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class VariableDataServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VariableDataServiceApplication.class, args);
	}
}
