package com.interview.camelkafkastarter.route;

import com.interview.camelkafkastarter.logic.MessageStrategy;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Random;


@Component(value = "n")
public class CamelKafkaRoute extends RouteBuilder {

    private final String KAFKA_URI = "kafka:first-topic?brokers=localhost:9092&groupId=myId&autoOffsetReset=earliest";

    public static final String PRODUCER_ROUTE = "producer-route";
    public static final String CONSUMER_ROUTE = "consumer-route";

    @Override
    public void configure() throws Exception {

        // producer route
        from("scheduler:pro?delay=10000").routeId(PRODUCER_ROUTE)
                .process(exchange -> exchange.getIn().setBody(createRandomNumber()))
                .to(KAFKA_URI).id("producer-to-kafka");

        // consumer routes
        from(this.KAFKA_URI).routeId(CONSUMER_ROUTE)
                .aggregate(new MessageStrategy())
                    .constant(true)
                    .completionInterval(60000)
                    .process(exchange -> System.out.println("consuming : " + exchange.getIn().getBody(Integer.class)));
    }

    private int createRandomNumber() {
        return new Random().nextInt(100);
    }
}
