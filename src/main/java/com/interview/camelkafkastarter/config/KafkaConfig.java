package com.interview.camelkafkastarter.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic topic() {
        System.out.println("topic method");
        return TopicBuilder.name("first-topic")
                .partitions(10)
                .replicas(1)
                .build();
    }

    @KafkaListener(id = "myId2", topics = "first-topic")
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
