package com.interview.camelkafkastarter.service;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class Producer {

    private final String KAFKA_URI = "kafka:first-topic?brokers=localhost:9092&groupId=myId&autoOffsetReset=earliest";

    private ProducerTemplate producerTemplate;

    @Autowired
    public Producer(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    public void sendMessage(){
        String message = String.valueOf(createRandomNumber());
        System.out.println("producing : " + message);
        producerTemplate.sendBody(this.KAFKA_URI, message);
    }

    private int createRandomNumber(){
        return new Random().nextInt(100);
    }
}
