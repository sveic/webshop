package com.example.demo.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.OrderItem;
import com.example.demo.repository.OrderItemRepository;

@Service
public class OrderItemService {
	private OrderItemRepository orderItemRepository;

	public OrderItemService(OrderItemRepository orderItemRepository) {
		super();
		this.orderItemRepository = orderItemRepository;
	}
	
	public OrderItem createOrderItem(OrderItem orderItem) {
		//TODO validate orderItem
		orderItemRepository.save(orderItem);
		return orderItem;
	}
	
	public List<OrderItem> getOrderItemsByOrderId(long orderId) {
		List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
		return orderItems;
	}
	
	public OrderItem getOrderItem(long id) throws ResourceNotFoundException {
		return orderItemRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("OrderItem not found for this id :: " + id));
		
	}
	public OrderItem updateOrderItem(long id, OrderItem newOrderItem) throws ResourceNotFoundException {
		//TODO validate orderItem
		OrderItem orderItem = getOrderItem(id);
		orderItem.setProduct(newOrderItem.getProduct());
		orderItem.setQuantity(newOrderItem.getQuantity());
	    orderItemRepository.save(orderItem);
		return orderItem;
		
	}
	public OrderItem deleteOrderItem(long id) throws ResourceNotFoundException {
		OrderItem orderItem = getOrderItem(id);
		orderItemRepository.delete(orderItem);
		return orderItem;
		
	}
}

