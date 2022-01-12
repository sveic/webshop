package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
@Table(name = "customer", schema = "webshop_schema")

public class Customer {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @NotNull
    @NotBlank(message = "FirstName is mandatory")
	@Column(name = "first_name", nullable = false)
	private String firstName;
	
   	@Column(name = "last_name")
	private String lastName;
	
	@Email
	@Size(max=30, message = "Code must have less then 30 characters")
	@Column(name = "email", length=30)
	private String email;
	
	/*@OneToMany(mappedBy = "customer", cascade = {CascadeType.ALL})
	private List < Order > orders;*/
	
	public Customer() {
	}

	public Customer(@NotNull @NotBlank(message = "FirstName is mandatory") String firstName, String lastName,
			@Email @Size(max = 30, message = "Code must have less then 30 characters") String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	

	
	
	
    
 
   
  
}