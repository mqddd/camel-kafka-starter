package com.interview.camelkafkastarter.route;

import com.interview.camelkafkastarter.CamelKafkaStarterApplication;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.concurrent.TimeUnit;


@ContextConfiguration
@SpringBootTest(classes = CamelKafkaStarterApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CamelKafkaRouteTest extends CamelSpringTestSupport {

    @Autowired
    private CamelContext context;

    @Autowired
    private ProducerTemplate template;

    @EndpointInject(value = "mock:final")
    private MockEndpoint result;

    @Test
    public void test() throws Exception {

        AdviceWithRouteBuilder.adviceWith(context, "kafka-route", routeBuilder -> {
            routeBuilder.replaceFromWith("direct:kafka-from");
        });

        result.expectedMessageCount(1);
        result.expectedBodiesReceived(6);

        template.sendBody("direct:kafka-from", 1);
//        Thread.sleep(10000);
        template.sendBody("direct:kafka-from", 1);
//        Thread.sleep(10000);
        template.sendBody("direct:kafka-from", 1);
//        Thread.sleep(10000);
        template.sendBody("direct:kafka-from", 1);
//        Thread.sleep(10000);
        template.sendBody("direct:kafka-from", 1);
//        Thread.sleep(10000);
        template.sendBody("direct:kafka-from", 1);
//        Thread.sleep(10000);

        MockEndpoint.assertIsSatisfied(150, TimeUnit.SECONDS, result);
    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return null;
    }
}