package org.interview.ordermanagement.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.interview.ordermanagement.constants.Side;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ordertbl")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue()
    //private UUID orderId;
    private Integer orderId;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private Side side;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    //private Double price;
    private Integer price;

    @Column(nullable = false)
    private String source;

    private Instant createdOn;

    private Instant updatedOn;
}
