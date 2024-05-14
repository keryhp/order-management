package org.interview.ordermanagement.dto;

import lombok.Builder;
import lombok.Data;
import org.interview.ordermanagement.constants.Side;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class OrderDTO {
    //private UUID orderId;
    private Integer orderId;

    private String symbol;

    private Side side;

    private Integer amount;

    //private Double price;
    private Integer price;

    private String source;

    private Instant createdOn;

    private Instant updatedOn;
}
