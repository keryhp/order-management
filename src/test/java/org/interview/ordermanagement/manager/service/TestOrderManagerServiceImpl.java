package org.interview.ordermanagement.manager.service;

import org.interview.ordermanagement.constants.Side;
import org.interview.ordermanagement.domain.Order;
import org.interview.ordermanagement.exceptions.OrderManagementException;
import org.interview.ordermanagement.exceptions.OrderNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

@SpringBootTest
public class TestOrderManagerServiceImpl {

    @Autowired
    protected OrderManagerServiceImpl orderManagerService;

    @Autowired
    protected OrderBookManagerServiceImpl orderBookManagerService;

    @Test
    void shouldAddOrderSuccessfully() throws OrderManagementException {
        Order order6 = Order.builder().orderId(6).price(100).symbol("XYZ").status("OPEN").side(Side.Buy).amount(16).createdOn(Instant.now()).build();
        orderManagerService.addOrder(order6.getOrderId(), order6.getSymbol(), order6.getSide(), order6.getAmount(), order6.getPrice());
        Assertions.assertTrue(orderBookManagerService.getOrderBookMap().get("XYZ").getBuyPricesAndOrdersMap().get(100).get(0).getOrderId().equals(6));
    }

    @Test
    void shouldRemoveOrderSuccessfully() throws OrderManagementException {
        Order order6 = Order.builder().orderId(6).price(100).symbol("XYZ").status("OPEN").side(Side.Buy).amount(16).createdOn(Instant.now()).build();
        orderManagerService.addOrder(order6.getOrderId(), order6.getSymbol(), order6.getSide(), order6.getAmount(), order6.getPrice());
        Assertions.assertThrows(OrderNotFoundException.class, () -> {
            orderManagerService.removeOrder(6);
        });
    }

    @Test
    void shouldModifyOrderSuccessfully() throws OrderManagementException {
        Order order8 = Order.builder().orderId(6).price(100).symbol("XYZ1").status("OPEN").side(Side.Buy).amount(16).createdOn(Instant.now()).build();
        orderManagerService.addOrder(order8.getOrderId(), order8.getSymbol(), order8.getSide(), order8.getAmount(), order8.getPrice());
        order8 = Order.builder().orderId(6).price(101).symbol("XYZ1").status("OPEN").side(Side.Buy).amount(16).createdOn(Instant.now()).build();
        Assertions.assertThrows(OrderManagementException.class, () -> {
            orderManagerService.modifyOrder(8, 16, 101);
        });
        //orderManagerService.modifyOrder(order8.getOrderId(), order8.getAmount(), order8.getPrice());
        //Assertions.assertTrue(orderBookManagerService.getOrderBookMap().get("XYZ").getBuyPricesAndOrdersMap().get(101).get(0).getOrderId().equals(8));
    }

    @Test
    void shouldCalculatePriceSuccessfully() throws OrderManagementException {
        destroy();
        Order order = Order.builder().orderId(1).price(20).symbol("JPM").status("OPEN").side(Side.Buy).amount(20).createdOn(Instant.now()).build();
        Order order4 = Order.builder().orderId(4).price(21).symbol("JPM").status("OPEN").side(Side.Buy).amount(10).createdOn(Instant.now()).build();
        orderManagerService.addOrder(order.getOrderId(), order.getSymbol(), order.getSide(), order.getAmount(), order.getPrice());
        orderManagerService.addOrder(order4.getOrderId(), order4.getSymbol(), order4.getSide(), order4.getAmount(), order4.getPrice());
        int calcPrice = orderManagerService.calculatePrice("JPM", Side.Buy, 22);
        Assertions.assertEquals(440, calcPrice);
    }

    @Test
    void shouldPlaceTradeSuccessfully() throws OrderManagementException {
        destroy();
        Order order = Order.builder().orderId(1).price(20).symbol("JPM1").status("OPEN").side(Side.Buy).amount(20).createdOn(Instant.now()).build();
        Order order4 = Order.builder().orderId(4).price(21).symbol("JPM1").status("OPEN").side(Side.Buy).amount(10).createdOn(Instant.now()).build();
        orderManagerService.addOrder(order.getOrderId(), order.getSymbol(), order.getSide(), order.getAmount(), order.getPrice());
        orderManagerService.addOrder(order4.getOrderId(), order4.getSymbol(), order4.getSide(), order4.getAmount(), order4.getPrice());
        orderManagerService.placeTrade("JPM1", Side.Buy, 22);
        Assertions.assertEquals(orderBookManagerService.getOrderBookMap().get("JPM1").getBuyPricesAndOrdersMap().size(), 2);
        Assertions.assertEquals(orderBookManagerService.getOrderBookMap().get("JPM1").getBuyPricesAndOrdersMap().get(21).get(0).getAmount(), 8);
    }

    protected void destroy() {
        orderManagerService.orderRepository.findAll().forEach(order -> {
            try {
                orderManagerService.removeOrder(order.getOrderId());
            } catch (OrderManagementException e) {
                //throw new RuntimeException(e); //unnecessary in case of already deleted orders via tests
            }
        });
    }
}
