package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class JpaSampleApplication {

	public static void main(String[] args) {
		log.info("Main System Start!!");
		SpringApplication.run(JpaSampleApplication.class, args);

	}

	@Bean
	Hibernate5Module hibernate5Module(){ //LAZY 로딩 무시
		Hibernate5Module hibernate5Module = new Hibernate5Module();
		/* //LAZY 로딩 다 가져옴
		hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
		*/
		return hibernate5Module;
	}

}
