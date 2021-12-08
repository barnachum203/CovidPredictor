package com.philips.project.msdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MsdbApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsdbApplication.class, args);
	}

	@Bean
	public RestTemplate initRestTemplate()
	{
		return new RestTemplate();
	}

}
