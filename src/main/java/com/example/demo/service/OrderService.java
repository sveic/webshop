package com.example.demo.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;

@Service
public class OrderService {
	private OrderRepository orderRepository;
	
	public OrderService(OrderRepository orderRepository ) {
		super();
		this.orderRepository = orderRepository;
		
	}
	
	public Order createOrder(Order order) {
		
		/*if (order.getOrderItems().size()>0) {
			order.setOrderItems(addOrderItems(order.getOrderItems()));
		 }*/
		Order newOrder = orderRepository.save(order);
		return newOrder;
	}
	
	public List<Order> getOrders() {
		List<Order> orders = orderRepository.findAll();
		return orders;
	}
	
	public List<Order> getOrdersByCustomerId(long customerId) {
		List<Order> orders = orderRepository.findByCustomerId(customerId);
		return orders;
	}
	
	public Order getOrder(long id) throws ResourceNotFoundException {
		return orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found for this id :: " + id));
		
	}
	public Order updateOrder(long id, Order newOrder) throws ResourceNotFoundException {
				
		//new order items
		/*if (newOrder.getOrderItems().size()>0) {
		  order.getOrderItems().clear();
		  order.setOrderItems(addOrderItems(newOrder.getOrderItems()));
		  }*/
		
		Order updatedOrder = orderRepository.save(newOrder);
		return updatedOrder;
		
	}
	public Order deleteOrder(long id) throws ResourceNotFoundException {
		Order order = getOrder(id);
		orderRepository.delete(order);
		return order;
		
	}
	
	
	/*public List<OrderItem> addOrderItems (List<OrderItem> items){
		 List<OrderItem> newItems = new ArrayList<OrderItem>();
		  for (OrderItem orderItem : items) {
			  OrderItem newOrderItem = orderItemService.createOrderItem(orderItem);
			  newItems.add(newOrderItem);
		  }
		  return newItems;
	}*/
	
	
}

