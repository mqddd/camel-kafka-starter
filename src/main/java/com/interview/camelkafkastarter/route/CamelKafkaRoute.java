package com.interview.camelkafkastarter.route;

import com.interview.camelkafkastarter.model.MessageStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


@Component
public class CamelKafkaRoute extends RouteBuilder {

    private final String KAFKA_URI = "kafka:first-topic?brokers=localhost:9092&groupId=myId&autoOffsetReset=earliest";

    @Override
    public void configure() throws Exception {

        from(this.KAFKA_URI)
                .aggregate(new MessageStrategy())
                .constant(true)
                .completionInterval(60000)
                .to("direct:somewhere");

        from("direct:somewhere")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        System.out.println(exchange.getIn().getBody(Integer.class));
                    }
                });
    }
}
