package com.interview.camelkafkastarter.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {

    @KafkaListener(topics = "first-topic", id = "myId")
    public void consume(String message) {
        System.out.println("Consuming message : " + message);
    }

}
