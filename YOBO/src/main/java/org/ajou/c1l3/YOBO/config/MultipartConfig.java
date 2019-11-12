package org.ajou.c1l3.YOBO.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

public class MultipartConfig {

    @Configuration
    @EnableWebMvc
    @ComponentScan(basePackages = { "org.ajou.c1l3.YOBO.controller" })
    public class WebConfig extends WebMvcConfigurerAdapter {
        private final int MAX_SIZE;
        {
            MAX_SIZE = 10485760;
        }

        @Bean
        public MultipartResolver multipartResolver() {
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
            multipartResolver.setMaxUploadSize(MAX_SIZE); // 10MB
            multipartResolver.setMaxUploadSizePerFile(MAX_SIZE); // 10MB
            multipartResolver.setMaxInMemorySize(0);
            return multipartResolver;
        }
    }
}
