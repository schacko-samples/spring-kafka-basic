package com.example;

import java.awt.print.Book;
import java.util.concurrent.CompletableFuture;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

@SpringBootApplication
public class SpringKafkaBasicApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringKafkaBasicApplication.class, args);
	}

	@Bean
	public NewTopic springKafkaApp0Demo1Topic() {
		return TopicBuilder.name("spring-kafka-demo-topic")
				.partitions(1)
				.replicas(3)
				.build();
	}

	@Bean
	public ApplicationRunner runner(KafkaTemplate<String, String> kafkaTemplate) {
		return args -> {
			for (int i = 0; i < 10; i++) {
				CompletableFuture<SendResult<String, String>> send = kafkaTemplate.send("spring-kafka-demo-topic",
						"This is demo data generated. Iteration: " + i);
				send.whenComplete((result, exception) -> {
					System.out.println("Data produced successfully.");
				});
			}
		};
	}

	@KafkaListener(id = "spring-kafka-demo-group", topics = "spring-kafka-demo-topic")
	public void listen(String in) {
		System.out.println("Data Received : " + in);
	}

}
