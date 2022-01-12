package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{
	public List<OrderItem> findByOrderId(long orderId);

}
