package com.photobooth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ============================================
 * Web MVC Configuration
 * ============================================
 * Configures static resource serving for uploaded files.
 * ============================================
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve uploaded photos from the /uploads/** path
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
