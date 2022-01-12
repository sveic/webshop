package com.example.demo;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;


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

import com.example.demo.model.OrderDto;
import com.example.demo.model.OrderItemDto;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebshopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerIntegrationTest {
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
     public void testGetAllOrders() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/orders",
        HttpMethod.GET, entity, String.class);  
        assertNotNull(response.getBody());
      }

    @Test
    public void testgetOrdersByCustomerId() {
    	HttpHeaders headers = new HttpHeaders();
    	HttpEntity<String> entity = new HttpEntity<String>(null, headers);
    	ResponseEntity<OrderDto[]> response = restTemplate.exchange(getRootUrl() +"/customers/1/orders",  
        HttpMethod.GET, entity, OrderDto[].class);
    	assertNotNull(response.getBody());
      }
    @Test
    public void testGetOrder() {
    	HttpHeaders headers = new HttpHeaders();
    	HttpEntity<String> entity = new HttpEntity<String>(null, headers);
    	ResponseEntity<OrderDto> response = restTemplate.exchange(getRootUrl() + "/orders/1",  
        HttpMethod.GET, entity, OrderDto.class);
    	assertNotNull(response.getBody());
        System.out.println(response.getBody().toString());
       
    }
 
    @Test
    public void testCreateOrder() {
    	HttpHeaders headers = new HttpHeaders();
        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(1);
        List <OrderItemDto> orderItemsDto = new ArrayList<OrderItemDto>();
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setProductId(1);
        orderItemDto.setQuantity(2);
        orderItemsDto.add(orderItemDto);
        orderItemDto = new OrderItemDto();
        orderItemDto.setProductId(2);
        orderItemDto.setQuantity(2);
        orderItemsDto.add(orderItemDto);
        orderDto.setOrderItemsDto(orderItemsDto);
        
        HttpEntity<OrderDto> requestBody = new HttpEntity<>(orderDto, headers);
        ResponseEntity<OrderDto> postResponse = restTemplate.exchange(getRootUrl() + "/orders", 
        HttpMethod.POST, requestBody, OrderDto.class);
        assertNotNull(postResponse);
        assertNotNull(postResponse.getBody());
    }

    @Test
    public void testUpdateOrder() {
        int id = 1;
        HttpHeaders headers = new HttpHeaders();
        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(2);
        List <OrderItemDto> orderItemsDto = new ArrayList<OrderItemDto>();
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setProductId(2);
        orderItemDto.setQuantity(3);
        orderItemsDto.add(orderItemDto);
        orderItemDto = new OrderItemDto();
        orderItemDto.setProductId(3);
        orderItemDto.setQuantity(1);
        orderItemsDto.add(orderItemDto);
        orderDto.setOrderItemsDto(orderItemsDto);
        HttpEntity<OrderDto> requestBody = new HttpEntity<>(orderDto, headers);
        ResponseEntity<OrderDto> putResponse = restTemplate.exchange(getRootUrl() + "/orders/"+id, 
        HttpMethod.PUT, requestBody, OrderDto.class);
        assertNotNull(putResponse);
        assertNotNull(putResponse.getBody());
    }

    @Test
    public void testFinalizeOrder() {
        int id = 3;
        HttpHeaders headers = new HttpHeaders();
        
        HttpEntity<String> requestBody = new HttpEntity<>(null, headers);
        ResponseEntity<OrderDto> putResponse = restTemplate.exchange(getRootUrl() + "/orders/"+id+"/finalize", 
        HttpMethod.PUT, requestBody, OrderDto.class);
        assertNotNull(putResponse);
        assertNotNull(putResponse.getBody());
    }

    
    @Test
    public void testDeleteOrder() {
         int id = 1;
         OrderDto orderDto = restTemplate.getForObject(getRootUrl() + "/orders/" + id, OrderDto.class);
         assertNotNull(orderDto);
         restTemplate.delete(getRootUrl() + "/orders/" + id);
         try {
        	 orderDto = restTemplate.getForObject(getRootUrl() + "/orders/" + id, OrderDto.class);
         } catch (final HttpClientErrorException e) {
              assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
         }
    }
}
