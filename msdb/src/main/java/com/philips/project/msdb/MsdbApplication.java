package com.philips.project.msdb;

import com.philips.project.msdb.services.Task;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
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

//	@Bean
//	public TaskExecutor taskExecutor() {
//		return new SimpleAsyncTaskExecutor(); // Or use another one of your liking
//	}
//
//	@Bean
//	public CommandLineRunner schedulingRunner(TaskExecutor executor) {
//		return new CommandLineRunner() {
//			public void run(String... args) throws Exception {
//				executor.execute(new Task());
//			}
//		}
//	}
}
