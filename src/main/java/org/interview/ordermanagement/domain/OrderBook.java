package org.interview.ordermanagement.domain;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

@Getter
@Setter
public class OrderBook {

    private final Logger LOGGER = LoggerFactory.getLogger(OrderBook.class);

    private String symbol;
    //natural order maintains buy at low price
    private Map<Integer, List<Order>> buyPricesAndOrdersMap;
    //reverse order maintains the sell at high price
    private Map<Integer, List<Order>> sellPricesAndOrdersMap;

    public OrderBook(String symbol) {
        this.symbol = symbol;
        buyPricesAndOrdersMap = new ConcurrentSkipListMap<>();
        sellPricesAndOrdersMap = new ConcurrentSkipListMap<>(Comparator.reverseOrder());
    }

    /**
     * adds Buy order to Order Book
     *
     * @param order
     */
    public void addBuyOrderToBook(Order order) {
        if (this.buyPricesAndOrdersMap.containsKey(order.getPrice())) {
            this.buyPricesAndOrdersMap.get(order.getPrice()).add(order);
        } else {
            this.buyPricesAndOrdersMap.put(order.getPrice(), addOrderToList(order));
        }
        LOGGER.debug("added BuyOrderToBook {} {}", symbol, order);
    }

    /**
     * adds Sell order to Order Book
     *
     * @param order
     */
    public void addSellOrderToBook(Order order) {
        if (this.sellPricesAndOrdersMap.containsKey(order.getPrice())) {
            this.sellPricesAndOrdersMap.get(order.getPrice()).add(order);
        } else {
            this.sellPricesAndOrdersMap.put(order.getPrice(), addOrderToList(order));
        }
        LOGGER.debug("added SellOrderToBook {} {}", symbol, order);
    }

    /**
     * removes Buy order from Order Book
     *
     * @param order
     */
    public void removeBuyOrderFromBook(Order order) {
        if (this.buyPricesAndOrdersMap.containsKey(order.getPrice())) {
            this.buyPricesAndOrdersMap.get(order.getPrice()).remove(order);
            LOGGER.debug("removed BuyOrderFromBook {} {}", symbol, order);
        } else {
            LOGGER.debug("Unable to remove Buy Order from OrderBook. Order is neither buy or sell");
        }

    }

    /**
     * removes Sell order from Order Book
     *
     * @param order
     */
    public void removeSellOrderFromBook(Order order) {
        if (this.sellPricesAndOrdersMap.containsKey(order.getPrice())) {
            this.sellPricesAndOrdersMap.get(order.getPrice()).remove(order);
            LOGGER.debug("removed SellOrderFromBook {} {}", symbol, order);
        } else {
            LOGGER.debug("Unable to remove Sell Order from OrderBook. Order is neither buy or sell");
        }
    }

    private List<Order> addOrderToList(Order order) {
        List<Order> orders = new LinkedList<>();
        orders.add(order);
        return orders;
    }
}
