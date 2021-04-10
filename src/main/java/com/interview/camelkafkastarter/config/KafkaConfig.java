package com.interview.camelkafkastarter.config;

import com.interview.camelkafkastarter.service.Producer;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    final
    Producer producer;

    @Autowired
    public KafkaConfig(Producer producer) {
        this.producer = producer;
    }

    @Bean
    public NewTopic topic() {
        System.out.println("topic method");
        return TopicBuilder.name("first-topic")
                .partitions(10)
                .replicas(1)
                .build();
    }

    @Bean
    public ApplicationRunner runner() {
        return args -> {
            this.producer.sendMessage();
        };
    }

}
