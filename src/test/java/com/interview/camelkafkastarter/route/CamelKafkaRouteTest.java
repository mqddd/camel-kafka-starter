package com.interview.camelkafkastarter.route;

import com.interview.camelkafkastarter.logic.MessageStrategy;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;


class CamelKafkaRouteTest extends CamelTestSupport {

    @Override
    protected RoutesBuilder[] createRouteBuilders() throws Exception {
        RouteBuilder consumerRouteBuilder = new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:fake-kafka")
                        .aggregate(new MessageStrategy())
                        .constant(true)
                        .completionInterval(60000)
                        .process(exchange -> System.out.println("consuming : " + exchange.getIn().getBody(Integer.class)))
                        .to("mock:consumer-end");
            }
        };
        RouteBuilder producerRoutBuilder = new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("scheduler:pro?delay=10000")
                        .process(exchange -> exchange.getIn().setBody(createRandomNumber()))
                        .to("mock:producer-end");
            }
        };
        return new RoutesBuilder[]{
                consumerRouteBuilder, producerRoutBuilder
        };
    }

    private int createRandomNumber() {
        return new Random().nextInt(100);
    }

    @Test
    public void producerTest() throws Exception {
        MockEndpoint result = getMockEndpoint("mock:producer-end");
        result.expectedMessageCount(6);
        MockEndpoint.assertIsSatisfied(60, TimeUnit.SECONDS, result);
    }

    @Test
    public void consumerTest() throws Exception {
        MockEndpoint result = getMockEndpoint("mock:consumer-end");
        result.expectedMessageCount(1);
        result.expectedBodiesReceived(21);
        template.sendBody("direct:fake-kafka", 1);
        template.sendBody("direct:fake-kafka", 6);
        template.sendBody("direct:fake-kafka", 5);
        template.sendBody("direct:fake-kafka", 4);
        template.sendBody("direct:fake-kafka", 3);
        template.sendBody("direct:fake-kafka", 2);
        MockEndpoint.assertIsSatisfied(60, TimeUnit.SECONDS, result);
    }

}