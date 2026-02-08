package com.example.ordersystem;

import org.springframework.boot.SpringApplication;

public class TestOrdersystemApplication {

	public static void main(String[] args) {
		SpringApplication.from(OrdersystemApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
