package com.invest.app.utilty.config;

import org.springframework.http.CacheControl;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;


import java.util.concurrent.TimeUnit;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    /*
    * html 위치 설정을 위한 핸들러 : 타임프로 대체
    * */
//    @Override
//    public void addResourceHandlers(final ResourceHandlerRegistry registry){
//        registry.addResourceHandler("/**")
//                .addResourceLocations("classPath:/templates/")
//                .setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES));
//    }
}
