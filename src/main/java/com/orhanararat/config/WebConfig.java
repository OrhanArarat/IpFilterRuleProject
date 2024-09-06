package com.orhanararat.config;

import com.orhanararat.interceptor.IPFilterInterceptor;
import lombok.RequiredArgsConstructor;
import org.kie.api.runtime.KieContainer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final KieContainer kieContainer;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new IPFilterInterceptor(kieContainer))
                .addPathPatterns("/**")
                .excludePathPatterns("/api/rules/**", "/swagger-ui/**", "/api-docs/**");
    }
}
