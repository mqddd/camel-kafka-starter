package com.interview.camelkafkastarter.logic;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;


public class MessageStrategy implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange != null) {
            Integer oldNumber = oldExchange.getIn().getBody(Integer.class);
            Integer newNumber = newExchange.getIn().getBody(Integer.class);
            newExchange.getIn().setBody(oldNumber + newNumber);
        }
        return newExchange;
    }
}
