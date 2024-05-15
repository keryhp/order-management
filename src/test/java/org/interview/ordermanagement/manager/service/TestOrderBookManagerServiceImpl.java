package org.interview.ordermanagement.manager.service;

import org.interview.ordermanagement.AbstractTest;
import org.interview.ordermanagement.constants.Side;
import org.interview.ordermanagement.domain.Order;
import org.interview.ordermanagement.exceptions.OrderManagementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;

@SpringBootTest
public class TestOrderBookManagerServiceImpl extends AbstractTest {

    @Test
    void shouldAddOrderToBook() throws OrderManagementException {
        Order order5 = Order.builder().orderId(5).price(75).symbol("ABC").status("OPEN").side(Side.Buy).amount(30).createdOn(Instant.now()).build();
        orderBookManagerService.addOrderToBook(order5);
        Order resOrder = orderBookManagerService.getOrderBookMap().get("ABC").getBuyPricesAndOrdersMap().get(75).get(0);
        Assertions.assertEquals(resOrder, order5);
    }

    @Test
    void shouldRemoveOrderToBook() throws OrderManagementException {
        Order order5 = Order.builder().orderId(5).price(75).symbol("ABC").status("OPEN").side(Side.Buy).amount(30).createdOn(Instant.now()).build();
        orderBookManagerService.removeOrderFromBook(order5);
        List<Order> resOrderList = orderBookManagerService.getOrderBookMap().get("ABC").getBuyPricesAndOrdersMap().get(75);
        Assertions.assertNull(resOrderList);
    }
}
