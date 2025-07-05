package io.github.fintechproject.mscartoes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class MscartoesApplication {
	public static void main(String[] args) {
		SpringApplication.run(MscartoesApplication.class, args);
	}
}
