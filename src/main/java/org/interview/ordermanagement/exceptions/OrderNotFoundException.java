package org.interview.ordermanagement.exceptions;

import org.interview.ordermanagement.constants.CommonConstants;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException() {
        super();
    }

    public OrderNotFoundException(Integer id) {
        super(CommonConstants.ORDER_REMOVAL_FAILED + " for " + id);
    }
}
