package com.example.demo.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


import lombok.Data;

@Data
@Entity
@Table(name = "order_item", schema = "webshop_schema")

public class OrderItem {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
	
	
	@ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "order_id")
    private Order order;

	/*@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;*/
	
	@ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "product_id")
    private Product product;
	
	 @Column(name = "quantity")
	 private int quantity;
	
	 
	 public static OrderItem from(OrderItemDto orderItemDto) {
		 OrderItem orderItem = new OrderItem();
		 orderItem.setId(orderItemDto.getId());
		 orderItem.setQuantity(orderItemDto.getQuantity());
		 		 
		 return orderItem;
	 }
}
