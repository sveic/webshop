package com.example.demo.controller;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

@RestController
@RequestMapping("/wshop/products")
public class ProductController {
	
	@Autowired
    private ProductRepository productRepository;

    @GetMapping()
    public List<Product> getAllProducts() {
    	   return productRepository.findAll();
    }

    @GetMapping("/query")
    public List<Product> getProductsByNameCode(@RequestParam(name="name", required=false) String name, @RequestParam(name="code", required=false) String code)  
		throws ResourceNotFoundException, IllegalArgumentException {
    	 if (name == null && code == null) throw new IllegalArgumentException("Product name or code must be sent");
    	 if (name != null && code != null) throw new IllegalArgumentException("Only one of them (product name or code) must be sent");
    	 if (name != null){
    	 List<Product> products = productRepository.findByName(name);
		  if (products.size()==0 ) throw   new ResourceNotFoundException("Product not found for this id :: + name");
		  return products;
    	 } else {
    		 Product product = productRepository.findByCode(code)
    		.orElseThrow(() -> new ResourceNotFoundException("Product not found for this code :: " + code));
    		 return Arrays.asList(product);
    	 } 
    	 
  }


  @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable(value = "id") Long productId)
        throws ResourceNotFoundException {
    	Product product = productRepository.findById(productId)
          .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + productId));
         return ResponseEntity.ok().body(product);
    }
       
    
    @PostMapping()
    public ResponseEntity<Product>  createProduct(@Valid @RequestBody Product product) throws IllegalArgumentException{
    	Optional<Product> productFound = productRepository.findByCode(product.getCode());
    	if (productFound.isPresent()) throw new IllegalArgumentException("Product with code:"+product.getCode()+ " already exists" );
    	String msgVal = product.validate();
    	if (msgVal != null) throw new IllegalArgumentException(msgVal);
        return new ResponseEntity<Product>(productRepository.save(product), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable(value = "id") Long productId,
         @Valid @RequestBody Product productDetails) throws ResourceNotFoundException, IllegalArgumentException {
    	
    	String msgVal = productDetails.validate();
    	if (msgVal != null) throw new IllegalArgumentException(msgVal);
    	
    	Optional<Product> productFound = productRepository.findByCode(productDetails.getCode());
    	if (productFound.isPresent() && productFound.get().getId() != productId) throw new IllegalArgumentException("Product with code:"+productDetails.getCode()+ " already exists" );
    	
    	Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + productId));

       
        product.setCode(productDetails.getCode());
        product.setName(productDetails.getName());
        product.setPriceHrk(productDetails.getPriceHrk());
        product.setDescription(productDetails.getDescription());
        product.setAvailable(productDetails.isAvailable());
        final Product updatedProduct = productRepository.save(product);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteProduct(@PathVariable(value = "id") Long productId)
         throws ResourceNotFoundException {
        Product product = productRepository.findById(productId)
       .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + productId));

        productRepository.delete(product);
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
