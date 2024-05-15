package org.interview.ordermanagement.manager.service;

import lombok.Getter;
import org.hibernate.dialect.unique.CreateTableUniqueDelegate;
import org.interview.ordermanagement.constants.CommonConstants;
import org.interview.ordermanagement.constants.Side;
import org.interview.ordermanagement.domain.Order;
import org.interview.ordermanagement.domain.OrderBook;
import org.interview.ordermanagement.exceptions.OrderManagementException;
import org.interview.ordermanagement.manager.OrderBookManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Service
public class OrderBookManagerServiceImpl implements OrderBookManager {

    private final Logger LOGGER = LoggerFactory.getLogger(OrderBookManagerServiceImpl.class);

    Map<String, OrderBook> orderBookMap = new ConcurrentHashMap<>();

    @Override
    public void addOrderToBook(Order order) throws OrderManagementException {
        LOGGER.debug("addOrderToBook {}", order);
        helpWithOrderBook(order, "add");
    }

    @Override
    public void removeOrderFromBook(Order order) throws OrderManagementException {
        LOGGER.debug("removeOrderFromBook {}", order);
        if(order != null) {
            helpWithOrderBook(order, "remove");
        }
    }

    private void helpWithOrderBook(Order order, String action) throws OrderManagementException {
        if(order.getSymbol() == null){
            String err = CommonConstants.ORDER_BOOK_ADD_ERROR + " Symbol is null " + order;
            LOGGER.error(err);
            throw new OrderManagementException(err);
        }
        if (!orderBookMap.containsKey(order.getSymbol())) {
            orderBookMap.put(order.getSymbol(), new OrderBook(order.getSymbol()));
        }
        OrderBook ob = orderBookMap.get(order.getSymbol());
        if (Side.Buy.equals(order.getSide())) {
            if ("add".equalsIgnoreCase(action)) {
                ob.addBuyOrderToBook(order);
            } else if ("remove".equalsIgnoreCase(action)) {
                ob.removeBuyOrderFromBook(order);
            }
        } else if (Side.Sell.equals(order.getSide())) {
            if ("add".equalsIgnoreCase(action)) {
                ob.addSellOrderToBook(order);
            } else if ("remove".equalsIgnoreCase(action)) {
                ob.removeSellOrderFromBook(order);
            }
        } else {
            String err = CommonConstants.ORDER_BOOK_ADD_ERROR + " Side is not Buy or Sell for " + order;
            LOGGER.error(err);
            throw new OrderManagementException(err);
        }
        LOGGER.debug("Order Book details: {}", ob);
    }
}
