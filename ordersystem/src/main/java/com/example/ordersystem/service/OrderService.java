package com.example.ordersystem.service;

import com.example.ordersystem.domain.Order;
import com.example.ordersystem.dto.OrderEvent;
import com.example.ordersystem.jpa.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository rep;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Transactional
    public void placeOrder(Order order){

        // save order object

        Order savedOrder=this.rep.save(order);
        // create dto using builder
        OrderEvent event=OrderEvent.builder()
                .orderId(savedOrder.getPo_id())
                .product(savedOrder.getProduct())
                .status(savedOrder.getOrderStatus().toString())
                .build();
        // send dto event to kafka
        kafkaTemplate.send("order-transactions",order.getPo_id().toString(),event);

    }



}
