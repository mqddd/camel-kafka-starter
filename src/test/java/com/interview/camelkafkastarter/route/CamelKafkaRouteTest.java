package com.interview.camelkafkastarter.route;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(CamelSpringBootRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
class CamelKafkaRouteTest extends CamelTestSupport {

    @Autowired
    private CamelContext camelContext;

    // first route params
    @Autowired
    private ProducerTemplate mockKafkaProducer;

    @EndpointInject(value = "mock:direct:somewhere")
    private MockEndpoint kafkaMock;

    // second route params
    @Autowired
    private ProducerTemplate directProducerTemplate;

    @EndpointInject(value = "mock:result")
    private MockEndpoint resultMock;

//    @Test
//    public void testKafkaRoute() throws Exception {
//        AdviceWithRouteBuilder.adviceWith(camelContext, CamelKafkaRoute.KAFKA_ROUTE_ID, routeBuilder -> {
//           routeBuilder.replaceFromWith("direct:from-kafka");
//        });
//
//        Map<String, Object> headers = new HashMap<>();
//        headers.put(KafkaConstants.TOPIC, "test-topic");
//
//        mockKafkaProducer.sendBodyAndHeaders("direct:from-kafka",1, headers);
//        mockKafkaProducer.sendBodyAndHeaders("direct:from-kafka",5, headers);
//        mockKafkaProducer.sendBodyAndHeaders("direct:from-kafka",6, headers);
//        mockKafkaProducer.sendBodyAndHeaders("direct:from-kafka",8, headers);
//        mockKafkaProducer.sendBodyAndHeaders("direct:from-kafka",9, headers);
//        mockKafkaProducer.sendBodyAndHeaders("direct:from-kafka",1, headers);
//
//        kafkaMock.expectedMessageCount(1);
//        kafkaMock.message(0).body().isInstanceOf(Integer.class);
//
//
//        //kafkaMock.expectedHeaderReceived(KafkaConstants.TOPIC, "test-topic");
//        kafkaMock.assertIsSatisfied();
//    }

    @Test
    public void testSecondRoute() throws InterruptedException {
        resultMock.setExpectedMessageCount(1);
        resultMock.message(0).body().isEqualTo(10);

        directProducerTemplate.sendBody("direct:somewhere", 10);

        resultMock.assertIsSatisfied();
    }
}