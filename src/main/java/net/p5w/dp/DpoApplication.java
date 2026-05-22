package net.p5w.dp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@MapperScan(basePackages = "net.p5w.dp.mapper")
@EnableScheduling
public class DpoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DpoApplication.class, args);
	}

}
