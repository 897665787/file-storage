package com.jqdi.filestorage.springbootdemo.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置
 */
@Configuration
public class CorsConfigurer implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// 跨域配置
		registry.addMapping("/**") // 对那些请求路径有效
				.allowedOrigins("*").allowedHeaders("*").allowedHeaders("*").allowCredentials(false).maxAge(1800L);
	}

}
