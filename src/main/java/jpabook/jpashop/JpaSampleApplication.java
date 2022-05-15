package jpabook.jpashop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class JpaSampleApplication {

	public static void main(String[] args) {
		log.info("Main System Start!!");
		SpringApplication.run(JpaSampleApplication.class, args);

	}

}
