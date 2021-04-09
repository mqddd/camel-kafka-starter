package com.interview.camelkafkastarter.service;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class CamelKafkaRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("kafka:first-topic?brokers=localhost:9092&groupId=myId&autoOffsetReset=earliest")
                .process(exchange -> System.out.println(exchange.getIn().getBody()))
                .log("event triggered!");
    }
}
