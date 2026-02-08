package com.example.ordersystem;

import com.example.ordersystem.domain.Order;
import com.example.ordersystem.domain.OrderStatus;
import com.example.ordersystem.dto.OrderEvent;
import com.example.ordersystem.jpa.OrderRepository;
import com.example.ordersystem.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void place_order_sucessfuly_kafka(){

        // create Order object to be used in the test
        Order order=Order
                .builder()
                .po_id(1L)
                .product("Laptop")
                .orderStatus(OrderStatus.PENDING)
                .build();

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // call the method
        orderService.placeOrder(order);


        // verify that object is saved and kafkaTemplate is called also
        verify(orderRepository,times(1)).save(any(Order.class));

        // verify templatkafka is called at least once
        verify(kafkaTemplate,times(1)).send(eq("order-transactions"),
                eq("1"),
                any(OrderEvent.class));

    }
}
