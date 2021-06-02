package com.rest.api;

import com.rest.api.kafka.Sender;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@EnableAspectJAutoProxy // AOP
@EnableCaching // 캐시
@EnableBatchProcessing // 배치
@EnableScheduling // 배치
@SpringBootApplication
public class ApiApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Autowired
	private Sender sender;

	@Override
	public void run(String... strings) throws Exception {
		// sender.send("Spring Kafka Producer and Consumer Example");
	}



	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.additionalInterceptors(new ClientHttpRequestInterceptor() {
			@Override
			public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
					throws IOException {
				request.getHeaders().set("Authorization", "Basic ODdjYmY0YjUtMWRjZC00OGEwLTkyZGEtODgzODNmZTgwZGQ5");
				return execution.execute(request, body);
			}
		}).build();
	}


}
