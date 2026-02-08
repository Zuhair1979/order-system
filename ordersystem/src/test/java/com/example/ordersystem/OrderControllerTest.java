package com.example.ordersystem;

import com.example.ordersystem.controller.OrderController;
import com.example.ordersystem.domain.Order;
import com.example.ordersystem.domain.OrderStatus;
import com.example.ordersystem.service.OrderService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)// slice test for Order controller, don't download other parts, service or database
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc; // we use mockmvc to send and retrun http request

    @MockitoBean
    private OrderService orderService; // virtual object to mock it

    @Autowired
    private ObjectMapper objectMapper;// this to serialized the object from to json, so we can send order object in http request body

    @Test
    public void returnIsCreatedStatus() throws Exception {
        // get order object
        Order order= Order.builder()
                .product("Laptop")
                .orderStatus(OrderStatus.CREATED)
                .build();

        doNothing().when(orderService).placeOrder(any(Order.class)); // when we call orderservice.placeorder(order) -> do nothing

        mockMvc.perform(post("/api/order/new")
                .content(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated());

    }


}
