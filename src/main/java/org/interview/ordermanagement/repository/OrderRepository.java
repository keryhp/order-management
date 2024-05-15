package org.interview.ordermanagement.repository;

import org.interview.ordermanagement.domain.Order;
import org.springframework.data.repository.CrudRepository;

//import java.util.UUID;

public interface OrderRepository extends CrudRepository<Order, Integer> {
}
