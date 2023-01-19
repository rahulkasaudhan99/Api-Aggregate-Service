package com.tnt.apiAggregation;

import java.util.concurrent.Executor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@SpringBootApplication
@EnableHystrix
public class AggregatorApplication {
	

	public static void main(String[] args) {
		SpringApplication.run(AggregatorApplication.class, args);
	}
	
	@Bean(name = "taskExecutor")
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setMaxPoolSize(200);
		executor.setCorePoolSize(200);
		executor.setQueueCapacity(600);
		executor.setThreadNamePrefix("concurrThread-");
		executor.initialize();
		executor.setKeepAliveSeconds(10);
		return executor;
	}
	
}
