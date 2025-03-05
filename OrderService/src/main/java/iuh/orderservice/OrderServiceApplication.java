package com.example.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@SpringBootApplication
@RestController
@RequestMapping("/orders")
public class OrderServiceApplication {

	private static final Map<Integer, String> orders = Map.of(
			1, "Order 1: Laptop",
			2, "Order 2: Smartphone",
			3, "Order 3: Tablet"
	);

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

	@GetMapping("/{id}")
	public String getOrderById(@PathVariable int id) {
		return orders.getOrDefault(id, "Order not found");
	}
}
