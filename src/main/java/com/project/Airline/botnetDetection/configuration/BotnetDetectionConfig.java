package com.project.Airline.botnetDetection.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableScheduling
public class BotnetDetectionConfig implements WebMvcConfigurer {

	private RequestsInterceptor requestsInterceptor;
	
	@Autowired
	public BotnetDetectionConfig(RequestsInterceptor requestsInterceptor) {
		this.requestsInterceptor = requestsInterceptor;
	}
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestsInterceptor);
    }
}
