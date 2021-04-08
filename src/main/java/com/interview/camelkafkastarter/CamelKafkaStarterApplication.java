package com.interview.camelkafkastarter;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;



@SpringBootApplication
public class CamelKafkaStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(CamelKafkaStarterApplication.class, args);
//        System.out.println("fhf");
    }

    @Bean
    public NewTopic topic() {
        System.out.println("topic method");
        return TopicBuilder.name("first-topic")
                .partitions(10)
                .replicas(1)
                .build();
    }

    @KafkaListener(id = "myId", topics = "first-topic")
    public void listen(String in) {
        System.out.println("in: " + in);
    }

    @Bean
    public ApplicationRunner runner(KafkaTemplate<String, String> template) {
        return args -> {
            template.send("first-topic", "test");
        };
    }

}
