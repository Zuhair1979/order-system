package com.example.ordersystem;


import com.example.ordersystem.domain.Order;
import com.example.ordersystem.domain.OrderStatus;
import com.example.ordersystem.jpa.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres=new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void test_OrderRepository_return_id(){
        Order order=Order.builder()
                .product("Laptop")
                .orderStatus(OrderStatus.PENDING)
                .build();
        Order savedOrder=orderRepository.save(order);
        assertThat(savedOrder.getPo_id()).isNotNull();// database generate id
        assertThat(orderRepository.findById(savedOrder.getPo_id())).isPresent();
    }

}
