package org.interview.ordermanagement.manager.service;

import org.interview.ordermanagement.constants.CommonConstants;
import org.interview.ordermanagement.constants.Side;
import org.interview.ordermanagement.domain.Order;
import org.interview.ordermanagement.domain.OrderBook;
import org.interview.ordermanagement.exceptions.OrderManagementException;
import org.interview.ordermanagement.exceptions.OrderNotFoundException;
import org.interview.ordermanagement.manager.OrderManager;
import org.interview.ordermanagement.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderManagerServiceImpl implements OrderManager {

    private final Logger LOGGER = LoggerFactory.getLogger(OrderManagerServiceImpl.class);

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderBookManagerServiceImpl orderBookManagerService;

    @Override
    public void addOrder(int orderId, String symbol, Side side, int amount, int price) throws OrderManagementException {
        LOGGER.debug("addOrder {}, {}, {}, {}, {}", orderId, symbol, side, amount, price);
        Order order = Order.builder().orderId(orderId).symbol(symbol).side(side).amount(amount).price(price).createdOn(Instant.now()).status(CommonConstants.OPEN).build();
        orderRepository.save(order);
        orderBookManagerService.addOrderToBook(order);
    }

    @Override
    public void removeOrder(int orderId) throws OrderManagementException {
        LOGGER.debug("removeOrder {}", orderId);
        Order order = this.orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        if (CommonConstants.OPEN.equalsIgnoreCase(order.getStatus())) {
            order.setStatus(CommonConstants.DELETED);
            order.setUpdatedOn(Instant.now());
            orderRepository.save(order);
            orderBookManagerService.removeOrderFromBook(order);
        } else {
            throw new OrderManagementException("Order " + order.getOrderId() + " cannot be deleted as it is already " + order.getStatus());
        }
    }

    @Override
    public void modifyOrder(int orderId, int amount, int price) throws OrderManagementException {
        LOGGER.debug("modifyOrder for new values orderId {}, amount {}, price {}", orderId, amount, price);
        Order order = this.orderRepository.findById(orderId).orElse(Order.builder().orderId(orderId).amount(amount).price(price).build()); //.orElseThrow(() -> new OrderNotFoundException(orderId));
        LOGGER.debug("Order found with values orderId {}, amount {}, price {}", orderId, amount, price);
        orderBookManagerService.removeOrderFromBook(order);
        order.setAmount(amount);
        order.setPrice(price);
        order.setUpdatedOn(Instant.now());
        if(amount == 0){
            order.setStatus(CommonConstants.ALLOCATED);
        }else if(amount < order.getAmount()){
            order.setStatus(CommonConstants.PROCESSING);
        }
        orderRepository.save(order);
        orderBookManagerService.addOrderToBook(order);
        LOGGER.debug("Order {} is modified successfully", orderId);
    }

    @Override
    public int calculatePrice(String symbol, Side side, int amount) throws OrderManagementException {
        LOGGER.debug("calculatePrice {}, {}, {}", symbol, side, amount);
        return calculatePriceAndBookTrade(symbol, side, amount, false);
    }

    @Override
    public void placeTrade(String symbol, Side side, int amount) throws OrderManagementException {
        LOGGER.debug("placeTrade {}, {}, {}", symbol, side, amount);
        calculatePriceAndBookTrade(symbol, side, amount, true);
    }

    private int calculatePriceAndBookTrade(String symbol, Side side, int amount, boolean bookTrade) throws OrderManagementException {
        OrderBook ob = orderBookManagerService.getOrderBookMap().get(symbol);
        List<Order> ordersToUpdate = new ArrayList<>();
        int resCalcPrice = 0;
        if (Side.Buy.equals(side)) {
            resCalcPrice = OrderPriceEngine.calculatePrice(ob.getBuyPricesAndOrdersMap(), amount, bookTrade, ordersToUpdate);
            cleanup(ordersToUpdate, bookTrade);
        } else if (Side.Sell.equals(side)) {
            resCalcPrice = OrderPriceEngine.calculatePrice(ob.getSellPricesAndOrdersMap(), amount, bookTrade, ordersToUpdate);
            cleanup(ordersToUpdate, bookTrade);
        } else {
            throw new OrderManagementException("Unable to calculate price for " + symbol + " " + side + " " + amount);
        }
        return resCalcPrice;
    }

    private void cleanup(List<Order> ordersToUpdate, boolean bookTrade){
        if(bookTrade){
            ordersToUpdate.forEach(order -> {
                try {
                    modifyOrder(order.getOrderId(),order.getAmount(), order.getPrice());
                } catch (OrderManagementException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
