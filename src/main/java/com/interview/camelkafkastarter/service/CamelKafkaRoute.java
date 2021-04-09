package com.interview.camelkafkastarter.service;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class CamelKafkaRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer:logMessageTimer?period=5s")
                .log("event triggered!");

    }
}
