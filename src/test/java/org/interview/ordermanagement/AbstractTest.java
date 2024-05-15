package org.interview.ordermanagement;

import org.interview.ordermanagement.constants.Side;
import org.interview.ordermanagement.domain.Order;
import org.interview.ordermanagement.domain.OrderBook;
import org.interview.ordermanagement.exceptions.OrderManagementException;
import org.interview.ordermanagement.manager.service.OrderBookManagerServiceImpl;
import org.interview.ordermanagement.manager.service.OrderManagerServiceImpl;
import org.interview.ordermanagement.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public abstract class AbstractTest {

    protected List<Order> orderList = new ArrayList<>();
    protected List<OrderBook> orderBookList;

    @Autowired
    protected OrderManagerServiceImpl orderManagerService;

    @Autowired
    protected OrderBookManagerServiceImpl orderBookManagerService;

    @Autowired
    protected OrderRepository orderRepository;

    @BeforeEach
    void initTest() throws OrderManagementException {
        orderList = new ArrayList<>();
        Order order = Order.builder().orderId(1).price(20).symbol("JPM").status("OPEN").side(Side.Buy).amount(20).createdOn(Instant.now()).build();
        Order order2 = Order.builder().orderId(2).price(25).symbol("GOOG").status("OPEN").side(Side.Buy).amount(10).createdOn(Instant.now()).build();
        Order order7 = Order.builder().orderId(2).price(10).symbol("GOOG").status("OPEN").side(Side.Buy).amount(20).createdOn(Instant.now()).build();
        Order order3 = Order.builder().orderId(3).price(10).symbol("AMZN").status("OPEN").side(Side.Buy).amount(7).createdOn(Instant.now()).build();
        Order order4 = Order.builder().orderId(4).price(21).symbol("JPM").status("OPEN").side(Side.Buy).amount(10).createdOn(Instant.now()).build();
        orderList.add(order);
        orderList.add(order2);
        orderList.add(order3);
        orderList.add(order4);
        orderList.add(order7);
        orderManagerService.addOrder(order.getOrderId(), order.getSymbol(), order.getSide(), order.getAmount(), order.getPrice());
        orderManagerService.addOrder(order2.getOrderId(), order2.getSymbol(), order2.getSide(), order2.getAmount(), order2.getPrice());
        orderManagerService.addOrder(order3.getOrderId(), order3.getSymbol(), order3.getSide(), order3.getAmount(), order3.getPrice());
        orderManagerService.addOrder(order4.getOrderId(), order4.getSymbol(), order4.getSide(), order4.getAmount(), order4.getPrice());
        orderManagerService.addOrder(order7.getOrderId(), order7.getSymbol(), order7.getSide(), order7.getAmount(), order7.getPrice());
    }

    @AfterEach
    protected void destroy() {
        orderList.forEach(order -> {
            try {
                orderManagerService.removeOrder(order.getOrderId());
            } catch (OrderManagementException e) {
                //throw new RuntimeException(e); //unnecessary in case of already deleted orders via tests
            }
        });
    }

}
