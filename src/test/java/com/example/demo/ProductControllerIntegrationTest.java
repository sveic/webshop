package com.example.demo;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import com.example.demo.model.Product;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebshopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerIntegrationTest {
     @Autowired
     private TestRestTemplate restTemplate;

     @LocalServerPort
     private int port;

     private String getRootUrl() {
         return "http://localhost:" + port+"/wshop";
     }

     @Test
     public void contextLoads() {

     }

     @Test
     public void testGetAllProducts() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/products",
        HttpMethod.GET, entity, String.class);  
        assertNotNull(response.getBody());
       
    }

    @Test
    public void testGetProductById() {
    	HttpHeaders headers = new HttpHeaders();
    	HttpEntity<String> entity = new HttpEntity<String>(null, headers);
    	ResponseEntity<Product> response = restTemplate.exchange(getRootUrl() + "/products/1",  
        HttpMethod.GET, entity, Product.class);
    	assertNotNull(response.getBody());
        System.out.println(response.getBody().getCode());
       
    }
    @Test
    public void testGetProductByNameCode() {
    	HttpHeaders headers = new HttpHeaders();
    	HttpEntity<String> entity = new HttpEntity<String>(null, headers);
    	ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/products/query?name=brašno",  
        HttpMethod.GET, entity, String.class);
    	assertNotNull(response.getBody());
     
    }
    
    @Test
    public void testCreateProduct() {
    	HttpHeaders headers = new HttpHeaders();
        Product product = new Product();
        product.setCode("2233344499");
        product.setName("brašno");
        product.setPriceHrk(34.80);
        HttpEntity<Product> requestBody = new HttpEntity<>(product, headers);
        ResponseEntity<Product> postResponse = restTemplate.exchange(getRootUrl() + "/products", 
        HttpMethod.POST, requestBody, Product.class);
        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());
    }

    @Test
    public void testUpdateProduct() {
        int id = 1;
        HttpHeaders headers = new HttpHeaders();
        Product product = new Product();
        product.setCode("1233344499");
        product.setName("brašno");
        HttpEntity<Product> requestBody = new HttpEntity<>(product, headers);
        ResponseEntity<Product> putResponse = restTemplate.exchange(getRootUrl() + "/products/"+id, 
        HttpMethod.PUT, requestBody, Product.class);
        assertNotNull(putResponse);
        assertNotNull(putResponse.getBody());
    }

    @Test
    public void testDeleteProduct() {
         int id = 2;
         Product product = restTemplate.getForObject(getRootUrl() + "/products/" + id, Product.class);
         assertNotNull(product);
         restTemplate.delete(getRootUrl() + "/products/" + id);
         try {
              product = restTemplate.getForObject(getRootUrl() + "/products/" + id, Product.class);
         } catch (final HttpClientErrorException e) {
              assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
         }
    }
}
