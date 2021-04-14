package com.interview.camelkafkastarter.route;

import com.interview.camelkafkastarter.logic.MessageStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Random;


@Component
public class CamelKafkaRoute extends RouteBuilder {

    private final String KAFKA_URI = "kafka:first-topic?brokers=localhost:9092&groupId=myId&autoOffsetReset=earliest";

    public static final String KAFKA_ROUTE_ID = "kafka-route";

    @Override
    public void configure() throws Exception {

        // producer route
        from("scheduler:pro?delay=10000")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        String message = String.valueOf(createRandomNumber());
                        System.out.println("producing : " + message);
                        exchange.getIn().setBody(message);
                    }
                })
                .to(KAFKA_URI);

        // consumer routes
        from(this.KAFKA_URI).routeId(KAFKA_ROUTE_ID)
                .aggregate(new MessageStrategy())
                .constant(true)
                .completionInterval(60000)
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        System.out.println("consuming : " + exchange.getIn().getBody(Integer.class));
                    }
                });
    }

    private int createRandomNumber(){
        return new Random().nextInt(100);
    }
}
