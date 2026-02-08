package com.example.ordersystem.controller;

import com.example.ordersystem.domain.Order;
import com.example.ordersystem.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    @PostMapping("/new")
    public ResponseEntity<String> createNewOrder(Order order){
        orderService.placeOrder(order);
        return ResponseEntity.ok("Order placed successfully and event sent to Kafka!");
    }
}