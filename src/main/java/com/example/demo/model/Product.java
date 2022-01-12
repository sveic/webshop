package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


import lombok.Data;

@Data
@Entity
@Table(name = "product", schema = "webshop_schema")

public class Product {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @NotNull
    @NotBlank(message = "code is mandatory")
    @Size(min = 10, max=10, message = "Code must have 10 characters")
    @Column(name = "code", length=10,//columnDefinition="CHARACTER(10) NOT NULL UNIQUE", 
	nullable = false, 
    unique = true   )
    private String code;
    
    @NotNull
    @NotBlank(message = "name is mandatory")
	@Column(name = "name", nullable = false)
	private String name;
	
    
	@Column(name = "price_hrk", columnDefinition="numeric(12,2)")
			//precision=12, scale=2)
	private double priceHrk;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "is_available", columnDefinition = "boolean default true")
	private boolean isAvailable=true;
	
		
	/*@OneToMany(mappedBy = "product", cascade = {CascadeType.ALL})
	private List < OrderItem > orderItems;*/

	public Product() {
	}

	public Product(String code, String name, double priceHrk, String description, boolean isAvailable) {
		super();
		this.code = code;
		this.name = name;
		this.priceHrk = priceHrk;
		this.description = description;
		this.isAvailable = isAvailable;
	}
	
	public String validate() throws IllegalArgumentException  {
		if (priceHrk < 0) return "Product id::"+id+ " must have priceHrk >=0" ;
		else return null;
	}
   
    
 
   
  
}