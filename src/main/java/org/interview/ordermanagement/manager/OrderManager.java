package org.interview.ordermanagement.manager;

import org.interview.ordermanagement.constants.Side;

/**
 * This interface is advised as per the coding challenge.
 */
public interface OrderManager {

    /**
     * adds order to the store
     * @param orderId
     * @param symbol
     * @param side
     * @param amount
     * @param price
     */
    void addOrder(int orderId, String symbol, Side side, int amount, int price);

    /**
     * removes order from the store by orderId
     * @param orderId
     */
    void removeOrder(int orderId);

    /**
     * modifies existing order by orderId
     * @param orderId
     * @param amount
     * @param price
     */
    void modifyOrder(int orderId, int amount, int price);

    /**
     * The best Buy price is the one of the order with smallest price in the book, multiplied by the requested amount. If the amount
     * of the order is not enough, then the next smallest priced order of the book should be added to the calculation and the
     * price calculated using the rest of the amount that could not be covered by the previous order. The best Sell price is calculated in a similar way, but the first order to be considered should be the one with highest
     * price instead of the lowest.
     * @param symbol
     * @param side
     * @param amount
     * @return
     */
    int calculatePrice(String symbol, Side side, int amount);

    /**
     * Finally, if a client is happy with the price, the system should place the trade. When a trade is placed, the application
     * should subtract the amount bought or sold to the client from each order. Empty orders, where amount is equal to zero,
     * should be removed from the order book.
     * @param symbol
     * @param side
     * @param amount
     */
    void placeTrade(String symbol, Side side, int amount);
}
