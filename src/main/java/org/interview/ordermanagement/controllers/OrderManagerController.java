package org.interview.ordermanagement.controllers;

import org.interview.ordermanagement.constants.CommonConstants;
import org.interview.ordermanagement.dto.OrderDTO;
import org.interview.ordermanagement.exceptions.OrderManagementException;
import org.interview.ordermanagement.manager.service.OrderManagerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
public class OrderManagerController {

    private final Logger LOGGER = LoggerFactory.getLogger(OrderManagerController.class);

    @Autowired
    OrderManagerServiceImpl orderManagerService;

    /**
     * REST API Controller to addOrder
     *
     * @param orderDTO
     * @return
     */
    @PostMapping(path = "/orders", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> addOrder(@RequestBody OrderDTO orderDTO) {
        try {
            orderDTO.setCreatedOn(Instant.now());
            orderManagerService.addOrder(orderDTO.getOrderId(), orderDTO.getSymbol(), orderDTO.getSide(), orderDTO.getAmount(), orderDTO.getPrice());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(CommonConstants.ORDER_ADDED_SUCCESSFULLY);
        } catch (OrderManagementException ex) {
            LOGGER.error("{} {}", CommonConstants.ORDER_ADD_FAILED, ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ex.getMessage());
        }
    }

    /**
     * REST API Controller to removeOrder by orderId
     *
     * @param orderId
     * @return
     */
    @DeleteMapping("/orders/{orderId}/delete")
    public ResponseEntity<?> removeOrder(@PathVariable Integer orderId) {
        try {
            orderManagerService.removeOrder(orderId);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(CommonConstants.ORDER_REMOVED_SUCCESSFULLY);
        } catch (OrderManagementException ex) {
            LOGGER.error("{} {}", CommonConstants.ORDER_REMOVAL_FAILED, ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ex.getMessage());
        }
    }

    @PutMapping("/orders/{orderId}")
    public ResponseEntity<?> modifyOrder(@PathVariable Integer orderId, @RequestBody OrderDTO orderDTO) {
        try {
            orderDTO.setOrderId(orderId);
            orderManagerService.modifyOrder(orderId, orderDTO.getAmount(), orderDTO.getPrice());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(CommonConstants.ORDER_MODIFIED_SUCCESSFULLY);
        } catch (OrderManagementException ex) {
            LOGGER.error("{} {}", CommonConstants.ORDER_MODIFY_FAILED, ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ex.getMessage());
        }
    }

    @PostMapping("/orders/{symbol}/price")
    public ResponseEntity<?> calculatePrice(@PathVariable String symbol, @RequestBody OrderDTO orderDTO) {
        try {
            int calculatedPrice = orderManagerService.calculatePrice(symbol, orderDTO.getSide(), orderDTO.getAmount());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(CommonConstants.CALC_PRICE_IS + calculatedPrice);
        } catch (OrderManagementException ex) {
            LOGGER.error("{} {}", CommonConstants.CALC_PRICE_FAILED, ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ex.getMessage());
        }
    }

    @PostMapping("/orders/{symbol}/trade")
    public ResponseEntity<?> placeTrade(@PathVariable String symbol, @RequestBody OrderDTO orderDTO) {
        try {
            orderManagerService.placeTrade(symbol, orderDTO.getSide(), orderDTO.getAmount());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(CommonConstants.PLACE_TRADE_SUCCESS);
        } catch (OrderManagementException ex) {
            LOGGER.error("{} {}", CommonConstants.PLACE_TRADE_FAILED, ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ex.getMessage());
        }
    }
}
