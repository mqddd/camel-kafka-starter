package com.interview.camelkafkastarter.service;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class Producer {

    private static final String TOPIC = "first-topic";

    private ProducerTemplate producerTemplate;

    @Autowired
    public Producer(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    @Scheduled(fixedRate = 10000)
    public void sendMessage(){
        String message = String.valueOf(createRandomNumber());
        System.out.println("producing : " + message);
        producerTemplate.sendBody("direct:kafka-producer", message);
    }

    private int createRandomNumber(){
        return new Random().nextInt(100);
    }
}
