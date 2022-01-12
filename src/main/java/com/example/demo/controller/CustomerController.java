package com.example.demo.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;

@RestController
@RequestMapping("/wshop/customers")
public class CustomerController {
	
	@Autowired
    private CustomerRepository customerRepository;

    @GetMapping()
    public List<Customer> getAllCustomers() {
    	   return customerRepository.findAll();
    }


  @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable(value = "id") Long customerId)
        throws ResourceNotFoundException {
    	Customer customer = customerRepository.findById(customerId)
          .orElseThrow(() -> new ResourceNotFoundException("Customer not found for this id :: " + customerId));
         return ResponseEntity.ok().body(customer);
    }
      
    @PostMapping()
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
    	  return new ResponseEntity<Customer>(customerRepository.save(customer), HttpStatus.CREATED);
    	
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable(value = "id") Long customerId,
         @Valid @RequestBody Customer customerDetails) throws ResourceNotFoundException {
    	    	
    	Customer customer = customerRepository.findById(customerId)
        .orElseThrow(() -> new ResourceNotFoundException("Customer not found for this id :: " + customerId));

       
    	customer.setFirstName(customerDetails.getFirstName());
    	customer.setLastName(customerDetails.getLastName());
    	customer.setEmail(customerDetails.getEmail());
       
        final Customer updatedCustomer = customerRepository.save(customer);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteCustomer(@PathVariable(value = "id") Long customerId)
         throws ResourceNotFoundException {
        Customer customer = customerRepository.findById(customerId)
       .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + customerId));

        customerRepository.delete(customer);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
      return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
