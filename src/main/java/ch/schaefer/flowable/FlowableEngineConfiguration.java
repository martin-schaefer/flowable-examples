package ch.schaefer.flowable;

import java.util.Collections;

import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.flowable.spring.boot.ProcessEngineAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@AutoConfigureAfter(ProcessEngineAutoConfiguration.class)
public class FlowableEngineConfiguration {

	private final ObjectMapper objectMapper;

	public FlowableEngineConfiguration(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Bean
	public EngineConfigurationConfigurer<SpringProcessEngineConfiguration> customProcessEngineConfigurer() {
		return engineConfiguration -> {
			engineConfiguration.setCustomPostVariableTypes(Collections.singletonList(new JsonType(objectMapper)));
//			VariableTypes variableTypes = engineConfiguration.getVariableTypes();
//			if (variableTypes == null) {
//				variableTypes = new DefaultVariableTypes();
//				engineConfiguration.setVariableTypes(variableTypes);
//			}
//			VariableType variableType = variableTypes.getVariableType("json");
//			if (variableType != null) {
//				variableTypes.removeType(variableType);
//			}
//			variableTypes.addType(new JsonType(objectMapper));
		};
	}

}
