package com.interview.camelkafkastarter.route;

import com.interview.camelkafkastarter.CamelKafkaStarterApplication;
import com.interview.camelkafkastarter.logic.MessageStrategy;
import org.apache.camel.*;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.InterceptSendToEndpointDefinition;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Random;
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

    @Test
    public void Test() throws Exception {

        MockEndpoint result = getMockEndpoint("mock:result");

        AdviceWith.adviceWith(context, CamelKafkaRoute.CONSUMER_ROUTE, a -> {
           a.replaceFromWith("direct:fake-kafka");
        });

//        context.getRoute("producer-route").setAutoStartup(false);  nullPointerError

        context.start();

        context.getRouteController().stopRoute("producer-route");

        result.expectedMessageCount(1);
        result.expectedBodiesReceived(21);
        template.sendBody("direct:fake-kafka", 1);
        template.sendBody("direct:fake-kafka", 6);
        template.sendBody("direct:fake-kafka", 5);
        template.sendBody("direct:fake-kafka", 4);
        template.sendBody("direct:fake-kafka", 3);
        template.sendBody("direct:fake-kafka", 2);
        MockEndpoint.assertIsSatisfied(60, TimeUnit.SECONDS, result);
//        context.getRouteController().stopRoute("consumer-route");
    }

//    @Test
//    public void producerTest() throws Exception {
//        MockEndpoint result = getMockEndpoint("mock:producer-end");
//        context.getRouteController().startRoute("producer-route");
//        result.expectedMessageCount(5);
//        MockEndpoint.assertIsSatisfied(20, TimeUnit.SECONDS, result);
//        context.getRouteController().stopRoute("producer-route");
//    }

}