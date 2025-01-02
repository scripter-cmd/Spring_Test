package com.billing.soft.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("soft/src/main/resources/static/invoices/**")
                .addResourceLocations("soft/src/main/resources/static/invoices"); // Adjust the path
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080/invoices/")  // Specify your allowed origins
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}