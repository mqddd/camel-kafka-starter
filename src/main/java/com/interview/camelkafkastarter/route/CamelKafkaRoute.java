package com.interview.camelkafkastarter.route;

import com.interview.camelkafkastarter.logic.MessageStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


@Component
public class CamelKafkaRoute extends RouteBuilder {

    private final String KAFKA_URI = "kafka:first-topic?brokers=localhost:9092&groupId=myId&autoOffsetReset=earliest";

    public static final String KAFKA_ROUTE_ID = "kafka-route";

    @Override
    public void configure() throws Exception {

        // consumer routes
        from(this.KAFKA_URI).routeId(KAFKA_ROUTE_ID)
                .aggregate(new MessageStrategy())
                .constant(true)
                .completionInterval(60000)
                .to("direct:somewhere");

        from("direct:somewhere")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        System.out.println("consuming sum : " + exchange.getIn().getBody(Integer.class));
                    }
                })
                .to("mock:result");

        // producer route
        from("direct:kafka-producer")
                .to(this.KAFKA_URI);
    }
}
