package com.mytech.mesapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
//import org.springframework.kafka.annotation.EnableKafka;


@SpringBootApplication
//@EnableKafka
public class MesapiApplication {


	public static void main(String[] args) {
		SpringApplication.run(MesapiApplication.class, args);
	}

}
