package org.example.springv3.core.config;

import jakarta.servlet.FilterRegistration;
import org.example.springv3.core.filter.JwtAuthorizationFilter;
import org.example.springv3.user.User;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

/*
@Controller, @RestController, @Service, @Repository, @Component, @Configuration 이것들이 ioc에 등록됨
 */

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<?> jwtAuthorizationFilter() {
        FilterRegistrationBean<JwtAuthorizationFilter> bean = new FilterRegistrationBean<>(new JwtAuthorizationFilter());
        bean.addUrlPatterns("/api/*");
        bean.setOrder(0);
        return bean;
    }
}
