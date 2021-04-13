package com.interview.camelkafkastarter.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

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

}
