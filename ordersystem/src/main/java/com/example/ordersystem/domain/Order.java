package com.example.ordersystem.domain;

import com.example.ordersystem.domain.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name="orders")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long po_id;

    private String product;
    private int quantity;
    private double price;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @PrePersist
    public void prePersist(){
        this.createdAt=LocalDateTime.now();
        if(this.orderStatus==null)
            this.orderStatus=OrderStatus.PENDING;
    }

}
