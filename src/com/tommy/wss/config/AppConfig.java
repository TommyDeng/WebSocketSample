package com.tommy.wss.config;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author TommyDeng <250575979@qq.com>
 * @version 创建时间：2016年9月13日 下午7:53:58
 *
 */
@Configuration
@EnableScheduling
@ComponentScan(basePackages = "com.tommy.wss")
public class AppConfig extends WebMvcConfigurationSupport {

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	public static void main(String[] args) {
		SpringApplication.run(AppConfig.class, args);
	}
}