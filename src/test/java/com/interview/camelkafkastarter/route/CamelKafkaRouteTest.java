package com.interview.camelkafkastarter.route;

import com.interview.camelkafkastarter.CamelKafkaStarterApplication;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
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
import java.util.concurrent.TimeUnit;


@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(classes = CamelKafkaStarterApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CamelKafkaRouteTest extends CamelTestSupport{

    @Autowired
    private ProducerTemplate template;

    @EndpointInject(value = "mock:final")
    private MockEndpoint result;

    @Test
    public void test() throws Exception {

        result.expectedMessageCount(1);
        result.expectedBodiesReceived(20);

        Map<String, Object> headers = new HashMap<>();
        headers.put(KafkaConstants.TOPIC, "first-topic");

        template.sendBodyAndHeaders("kafka:first-topic?brokers=localhost:9092&groupId=myId&autoOffsetReset=earliest",1, headers);
        Thread.sleep(10000);
        template.sendBodyAndHeaders("kafka:first-topic?brokers=localhost:9092&groupId=myId&autoOffsetReset=earliest",5, headers);
        Thread.sleep(10000);
        template.sendBodyAndHeaders("kafka:first-topic?brokers=localhost:9092&groupId=myId&autoOffsetReset=earliest",6, headers);
        Thread.sleep(10000);
        template.sendBodyAndHeaders("kafka:first-topic?brokers=localhost:9092&groupId=myId&autoOffsetReset=earliest",8, headers);
        Thread.sleep(10000);
        template.sendBodyAndHeaders("kafka:first-topic?brokers=localhost:9092&groupId=myId&autoOffsetReset=earliest",9, headers);
        Thread.sleep(10000);
        template.sendBodyAndHeaders("kafka:first-topic?brokers=localhost:9092&groupId=myId&autoOffsetReset=earliest",1, headers);

        MockEndpoint.assertIsSatisfied(65, TimeUnit.SECONDS);
    }
}