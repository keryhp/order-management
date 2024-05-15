package org.interview.ordermanagement.manager.service;

import org.interview.ordermanagement.constants.CommonConstants;
import org.interview.ordermanagement.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class OrderPriceEngine {

    private final Logger LOGGER = LoggerFactory.getLogger(OrderPriceEngine.class);

    /**
     * This calculates the best price for a given symbol and amount and covers both Buy and Sell scenarios. The best Buy price is the one of the order with smallest price in the book,
     * multiplied by the requested amount. If the amount of the order is not enough, then the next smallest priced order of the book should be added
     * to the calculation and the price calculated using the rest of the amount that could not be covered by the previous order.
     * The best Sell price is calculated in a similar way, but the first order to be considered should be the one with highest price instead of the lowest.
     * @param priceOrderMap
     * @param amount
     * @param bookTrade
     * @return
     */
    public static int calculatePrice(Map<Integer, List<Order>> priceOrderMap, int amount, boolean bookTrade, List<Order> ordersToUpdate) {
        AtomicReference<Integer> calculatedPrice = new AtomicReference<>(0);
        AtomicReference<Integer> qtyMatched = new AtomicReference<>(0);
        priceOrderMap.keySet().forEach(price -> {
            for (Order order : priceOrderMap.get(price)) {
                if ((qtyMatched.get() < amount) && (qtyMatched.get() + order.getAmount() <= amount)) {
                    qtyMatched.updateAndGet(v -> v + order.getAmount());
                    calculatedPrice.updateAndGet(v -> v + (Math.multiplyExact(order.getAmount(), price)));
                    if(bookTrade){
                        order.setAmount(0);
                        ordersToUpdate.add(order);
                        //priceOrderMap.get(price).remove(order);
                    }
                } else if ((qtyMatched.get() < amount) && (qtyMatched.get() + order.getAmount() > amount)) {
                    int amountReq = amount - qtyMatched.get();
                    qtyMatched.updateAndGet(v -> v + amountReq);
                    calculatedPrice.updateAndGet(v -> v + (Math.multiplyExact(amountReq, price)));
                    if(bookTrade){
                        order.setAmount(order.getAmount() - amountReq);
                        ordersToUpdate.add(order);
                    }
                }
                if (qtyMatched.get() == amount) {
                    break;
                }
            }
        });
        return calculatedPrice.get();
    }

    /**
     * Calculates Price and then subtract the amount bought or sold to the client from each order. Empty orders, where amount is equal to zero,
     * should be removed from the order book.
     * @param priceOrderMap
     * @param amount
     * @return
     */
    public static int placeTrade(Map<Integer, List<Order>> priceOrderMap, int amount, List<Order> ordersToUpdate) {
        return calculatePrice(priceOrderMap, amount, true, ordersToUpdate);
    }
}
