package org.interview.ordermanagement.manager.service;

import org.interview.ordermanagement.constants.Side;
import org.interview.ordermanagement.domain.Order;
import org.interview.ordermanagement.manager.OrderManager;
import org.interview.ordermanagement.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class OrderManagerServiceImpl implements OrderManager {

    private final Logger LOGGER = LoggerFactory.getLogger(OrderManagerServiceImpl.class);

    @Autowired
    OrderRepository orderRepository;

    @Override
    public void addOrder(int orderId, String symbol, Side side, int amount, int price) {
        LOGGER.debug("addOrder {}, {}, {}, {}, {}", orderId, symbol, side, amount, price);
        Order order = Order.builder().orderId(orderId).symbol(symbol).side(side).amount(amount).price(price).createdOn(Instant.now()).build();
        orderRepository.save(order);
    }

    @Override
    public void removeOrder(int orderId) {
        LOGGER.debug("removeOrder {}", orderId);
        orderRepository.deleteById(orderId);
    }

    @Override
    public void modifyOrder(int orderId, int amount, int price) {
        LOGGER.debug("modifyOrder for new values orderId {}, amount {}, price {}", orderId, amount, price);
        if(orderRepository.existsById(orderId)) {
            Order order = orderRepository.findById(orderId).get();
            LOGGER.debug("Order found with values orderId {}, amount {}, price {}", orderId, amount, price);
            order.setAmount(amount);
            order.setPrice(price);
            order.setUpdatedOn(Instant.now());
            orderRepository.save(order);
            LOGGER.debug("Order {} is modified successfully", orderId);
        }
    }

    @Override
    public int calculatePrice(String symbol, Side side, int amount) {
        LOGGER.debug("calculatePrice {}, {}, {}", symbol, side, amount);
        return 0;
    }

    @Override
    public void placeTrade(String symbol, Side side, int amount) {
        LOGGER.debug("placeTrade {}, {}, {}", symbol, side, amount);
    }
}
