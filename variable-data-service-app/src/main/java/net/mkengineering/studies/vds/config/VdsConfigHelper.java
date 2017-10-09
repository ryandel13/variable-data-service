package net.mkengineering.studies.vds.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class VdsConfigHelper {

	@Autowired
	private VdsConfig vdsConfig;
	
	private static VdsConfig vdsConfigStatic;
	
	@Bean
	private Object VdsConfigHelperConstructor() {
		vdsConfigStatic = vdsConfig;
		return vdsConfigStatic;
	}
	
	public static VdsConfig getConfig() {
		if(vdsConfigStatic == null) {
			VdsConfigHelper configHelper = new VdsConfigHelper();
			VdsConfigHelper.vdsConfigStatic = configHelper.getConfigFromClass();
		}
		return vdsConfigStatic;
	}

	private VdsConfig getConfigFromClass() {
		return this.vdsConfig;
	}
	
}
