package com.interview.camelkafkastarter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class Producer {

    private static final String TOPIC = "first-topic";

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public Producer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedRate = 10000)
    public void sendMessage(){
        String message = String.valueOf(createRandomNumber());
        System.out.println("producing : " + message);
        this.kafkaTemplate.send(TOPIC, message);
    }

    private int createRandomNumber(){
        return new Random().nextInt(100);
    }
}
