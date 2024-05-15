package org.interview.ordermanagement.manager;

import org.interview.ordermanagement.domain.Order;
import org.interview.ordermanagement.exceptions.OrderManagementException;

public interface OrderBookManager {

    /**
     * adds Order to Order Book based on appropriate Buy or Sell Side
     *
     * @param order
     * @throws OrderManagementException
     */
    void addOrderToBook(Order order) throws OrderManagementException;

    /**
     * removes Order from Order Book based on appropriate Buy or Sell Side
     *
     * @param order
     * @throws OrderManagementException
     */
    void removeOrderFromBook(Order order) throws OrderManagementException;
}
