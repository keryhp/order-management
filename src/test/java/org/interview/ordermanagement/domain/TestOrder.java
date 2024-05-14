package org.interview.ordermanagement.domain;

import org.interview.ordermanagement.constants.Side;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class TestOrder {

    private Order order;

    @Test
    public void testOrderObjectIsWellFormed() {
        order = Order.builder().orderId(1).symbol("ABC").side(Side.Buy).amount(1).price(1).source("Test").createdOn(Instant.now()).build();
        Assertions.assertEquals("ABC", order.getSymbol());
        Assertions.assertEquals("Test", order.getSource());
        Assertions.assertEquals(Side.Buy, order.getSide());
        Assertions.assertEquals(1, order.getAmount());
        Assertions.assertEquals(1, order.getPrice());
        Assertions.assertNotNull(order.getCreatedOn());
        Assertions.assertNull(order.getUpdatedOn());
    }
}
