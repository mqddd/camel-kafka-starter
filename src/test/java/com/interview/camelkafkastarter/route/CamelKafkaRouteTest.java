package com.interview.camelkafkastarter.route;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;


@RunWith(CamelSpringBootRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
class CamelKafkaRouteTest extends CamelTestSupport {

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

    @Test
    public void testSecondRoute() throws InterruptedException {
        resultMock.setExpectedMessageCount(1);
        resultMock.message(0).body().isEqualTo(10);

        directProducerTemplate.sendBody("direct:somewhere", 10);

        resultMock.assertIsSatisfied();
    }
}