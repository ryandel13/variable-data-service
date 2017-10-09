package net.mkengineering.studies.vds.config;

import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class VdsConfig {

	private Integer maxHistory = 10;
	
}
