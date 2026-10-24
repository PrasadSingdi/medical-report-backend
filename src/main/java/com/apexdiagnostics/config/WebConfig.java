package com.apexdiagnostics.config;

import com.apexdiagnostics.common.CorrelationIdFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${app.cors-origin}")
    private String corsOrigin;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**").allowedOrigins(corsOrigin).allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").allowedHeaders("Authorization", "Content-Type", "X-Correlation-ID");
    }

    @Bean
    FilterRegistrationBean<CorrelationIdFilter> correlationIdFilter() {
        var bean = new FilterRegistrationBean<>(new CorrelationIdFilter());
        bean.setOrder(1);
        return bean;
    }
}
