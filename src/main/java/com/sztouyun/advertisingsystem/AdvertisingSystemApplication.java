package com.sztouyun.advertisingsystem;

import org.joda.time.DateTimeZone;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;

import java.util.TimeZone;

@Controller
@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication
@EnableCaching
@EnableGlobalMethodSecurity(prePostEnabled=true)
@MapperScan("com.sztouyun.advertisingsystem.mapper")
public class AdvertisingSystemApplication {
	public static void main(String[] args) {
		SpringApplication springApplication =new SpringApplication(AdvertisingSystemApplication.class);
		springApplication.addListeners(new ApplicationStartup());
		init();
		springApplication.run(args);
	}

	public static void  init(){
		DateTimeZone.setDefault(DateTimeZone.forTimeZone(TimeZone.getTimeZone("GMT+8")));
	}

}
