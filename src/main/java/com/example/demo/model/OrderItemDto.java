package com.example.demo.model;


import lombok.Data;

@Data
public class OrderItemDto {
	
    private long id;
	private long orderId;
    private long productId;
	private int quantity;
	
	public static OrderItemDto from(OrderItem orderItem) {
		OrderItemDto orderItemDto = new OrderItemDto();
		orderItemDto.setId(orderItem.getId());
		orderItemDto.setOrderId(orderItem.getOrder().getId());
		orderItemDto.setProductId(orderItem.getProduct().getId());
		orderItemDto.setQuantity(orderItem.getQuantity());
	    return orderItemDto;
	}
}
