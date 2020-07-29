package com.chason.bossdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan("com.chason.bossdemo.domain.*.repository")
@ServletComponentScan
public class BossDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BossDemoApplication.class, args);
	}

}
