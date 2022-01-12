package com.example.demo.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


import lombok.Data;

@Data
@Entity
@Table(name = "webshop_order", schema = "webshop_schema")

public class Order {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
	
	
	@ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "customer_id")
    private Customer customer;

	@OneToMany(mappedBy = "order", cascade = {CascadeType.ALL})
	private List < OrderItem > orderItems;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 30)
	private Status status;
	
	@Column(name = "total_price_hrk", columnDefinition="numeric(12,2)")
	private double totalPriceHrk;
	
	@Column(name = "total_price_eur", columnDefinition="numeric(12,2)")
	private double totalPriceEur;

	public Order() {  }
	
	public Order(Customer customer, List<OrderItem> orderItems, Status status, double totalPriceHrk,
			double totalPriceEur) {
		super();
		this.customer = customer;
		this.orderItems = orderItems;
		this.status = status;
		this.totalPriceHrk = totalPriceHrk;
		this.totalPriceEur = totalPriceEur;
	}
	
	public static Order from(OrderDto orderDto) {
		Order order = new Order();
		order.setId(orderDto.getId());
		order.setStatus(orderDto.getStatus());
		order.setTotalPriceHrk(orderDto.getTotalPriceHrk());
		order.setTotalPriceEur(orderDto.getTotalPriceEur());
		return order;
	}

  
}