package com.interview.camelkafkastarter.route;

import org.apache.camel.*;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ProcessDefinition;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.TimeUnit;

@ContextConfiguration
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CamelKafkaRouteTest extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new CamelKafkaRoute();
    }

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @BeforeEach
    public void setMockForKafka() throws Exception {
        AdviceWith.adviceWith(context, CamelKafkaRoute.PRODUCER_ROUTE, a -> {
            a.weaveById("producer-to-kafka").replace().to("mock:producer-end");
        });
    }

    @Test
    @Order(1)
    public void consumerTest() throws Exception {

        AdviceWith.adviceWith(context, CamelKafkaRoute.CONSUMER_ROUTE, a -> {
           a.replaceFromWith("direct:fake-kafka");
           a.weaveByType(ProcessDefinition.class).after().to("mock:result");
        });

        context.getRouteDefinition("producer-route").autoStartup(false);

        context.start();

        MockEndpoint result = getMockEndpoint("mock:result");

        result.expectedMessageCount(1);
        result.expectedBodiesReceived(21);

        template.sendBody("direct:fake-kafka", 1);
        template.sendBody("direct:fake-kafka", 6);
        template.sendBody("direct:fake-kafka", 5);
        template.sendBody("direct:fake-kafka", 4);
        template.sendBody("direct:fake-kafka", 3);
        template.sendBody("direct:fake-kafka", 2);

        MockEndpoint.assertIsSatisfied(60, TimeUnit.SECONDS, result);

        context.getRouteController().stopRoute("consumer-route");
    }

    @Test
    @Order(2)
    public void producerTest() throws Exception {

        context.getRouteDefinition("consumer-route").autoStartup(false);

        context.start();

        MockEndpoint result = getMockEndpoint("mock:producer-end");

        result.expectedMessageCount(3);

        MockEndpoint.assertIsSatisfied(23, TimeUnit.SECONDS, result);
    }

}