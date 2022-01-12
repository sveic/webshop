package com.example.demo.model;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class OrderDto {
	
    private long id;
	private long customerId;
	private List < OrderItemDto > orderItemsDto;
	private Status status;
	private double totalPriceHrk;
	private double totalPriceEur;
	
	public static OrderDto from(Order order) {
		OrderDto orderDto = new OrderDto();
		orderDto.setId(order.getId());
		orderDto.setCustomerId(order.getCustomer().getId());
		orderDto.setStatus(order.getStatus());
		orderDto.setOrderItemsDto(order.getOrderItems().stream()
				.map(OrderItemDto::from)
				.collect(Collectors.toList()) );
		orderDto.setTotalPriceHrk(order.getTotalPriceHrk());
		orderDto.setTotalPriceEur(order.getTotalPriceEur());
		return orderDto;
	}

}
