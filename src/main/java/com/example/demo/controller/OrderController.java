package com.example.demo.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
//import org.springframework.web.client.RestTemplate;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Customer;
import com.example.demo.model.Order;
import com.example.demo.model.OrderDto;
import com.example.demo.model.OrderItem;
import com.example.demo.model.OrderItemDto;
import com.example.demo.model.Product;
import com.example.demo.model.Status;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.OrderItemService;
import com.example.demo.service.OrderService;
import com.example.demo.webClient.WebClientService;


@RestController
@RequestMapping("/wshop")
public class OrderController {
		
		@Autowired
	    private OrderService orderService;
		
		@Autowired
	    private OrderItemService orderItemService;
		
		@Autowired
	    private CustomerRepository customerRepository;
		
		@Autowired
	    private ProductRepository productRepository;
		
		@Autowired
		private WebClientService webClientService;


	    @GetMapping("/orders")
	    public List<OrderDto> getAllOrders() {
	    	List<Order>orders = orderService.getOrders();
	    	List<OrderDto> ordersDto = orders.stream()
	    				.map(OrderDto::from)
	    				.collect(Collectors.toList()); 
	    	return ordersDto;	    	
	    }
	    
	    @GetMapping("customers/{customerId}/orders")
	    public List<OrderDto> getOrdersByCustomerId(@PathVariable(value = "customerId") Long customerId) {
	    	List<Order>orders = orderService.getOrdersByCustomerId(customerId);
	    	List<OrderDto> ordersDto = orders.stream()
	    				.map(OrderDto::from)
	    				.collect(Collectors.toList()); 
	    	return ordersDto;	    	
	    }

	
	  @GetMapping("/orders/{id}")
	    public ResponseEntity<OrderDto> getOrder(@PathVariable(value = "id") Long id)
	        throws ResourceNotFoundException {
	    	Order order = orderService.getOrder(id);
	        return ResponseEntity.ok().body(OrderDto.from(order));
	    }
	  
	  
	  	    
	    @PostMapping("/orders")
	    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderDto orderDto) throws ResourceNotFoundException, IllegalArgumentException{
	    	//find customer
   		    Customer customer = customerRepository.findById(orderDto.getCustomerId())
   		    .orElseThrow(() -> new ResourceNotFoundException("Customer not found for this id :: " + orderDto.getCustomerId()));
	    	
   		    Order order = Order.from(orderDto);
   		    order.setCustomer(customer);
   		    order.setStatus(Status.DRAFT);
   		    //create order
   		    Order newOrder = orderService.createOrder(order);
	    	List<OrderItemDto> orderItemsDto = orderDto.getOrderItemsDto();
	    	List<OrderItem> orderItems = new ArrayList<OrderItem>();
	    	for (OrderItemDto orderItemDto : orderItemsDto) {
	    		//find product
	    		 Product product = productRepository.findById(orderItemDto.getProductId())
	    		 .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + orderItemDto.getProductId()));
	    		//test availability
	    		 if (!product.isAvailable()) throw new IllegalArgumentException("Product for this id :: " + orderItemDto.getProductId()+ " is not available");
	    		OrderItem orderItem=OrderItem.from(orderItemDto);
	    		orderItem.setProduct(product);
	    		orderItem.setOrder(newOrder);
	    		//create orderItem
	    		OrderItem newOrderItem = orderItemService.createOrderItem(orderItem);
	    		orderItems.add(newOrderItem);
	    	}
	    	newOrder.setOrderItems(orderItems);
	    	return new ResponseEntity<OrderDto>(OrderDto.from(newOrder), HttpStatus.CREATED);
	    
	    }

	    @PutMapping("/orders/{id}")
	    public ResponseEntity<OrderDto> updateOrder(@PathVariable(value = "id") Long id,
	         @Valid @RequestBody OrderDto orderDto) throws ResourceNotFoundException, IllegalArgumentException {
	    	//find Order	    	
	    	 orderService.getOrder(id);
	    	//find customer
	   		 Customer customer = customerRepository.findById(orderDto.getCustomerId())
	   		 .orElseThrow(() -> new ResourceNotFoundException("Customer not found for this id :: " + orderDto.getCustomerId()));
	   		Order newOrder = Order.from(orderDto);
	   		newOrder.setId(id);
	   		newOrder.setCustomer(customer);	
	   		
	   	    //delete old order items
	   		List<OrderItem> oldOrderItems = orderItemService.getOrderItemsByOrderId(id);
	   		for (OrderItem orderItem : oldOrderItems) {
	   			orderItemService.deleteOrderItem(orderItem.getId());
	   	  	}
	   			   		
	   		//create new order items
	   		List<OrderItemDto> orderItemsDto = orderDto.getOrderItemsDto();
	   		List<OrderItem> orderItems = new ArrayList<OrderItem>();
	    	for (OrderItemDto orderItemDto : orderItemsDto) {
	    		//find product
	    		 Product product = productRepository.findById(orderItemDto.getProductId())
	    		 .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + orderItemDto.getProductId()));
	    		//test availability
	    		 if (!product.isAvailable()) throw new IllegalArgumentException("Product for this id :: " + orderItemDto.getProductId()+ " is not available");
	    		OrderItem orderItem=OrderItem.from(orderItemDto);
	    		orderItem.setProduct(product);
	    		orderItem.setOrder(newOrder);
	    		//create orderItem
	    		OrderItem newOrderItem = orderItemService.createOrderItem(orderItem);
	    		orderItems.add(newOrderItem);
	    	}
	    	newOrder.setOrderItems(orderItems);
	    	orderService.updateOrder(newOrder.getId(), newOrder);
	              
	        return ResponseEntity.ok(OrderDto.from(newOrder));
	    }

	    @PutMapping("/orders/{id}/finalize")
	    public ResponseEntity<OrderDto> finalizeOrder(@PathVariable(value = "id") Long id
	         ) throws ResourceNotFoundException, IllegalArgumentException {
	    	//find Order	    	
	    	 Order order = orderService.getOrder(id);
	    		   		
	   		List<OrderItem> orderItems = order.getOrderItems();
	   		//counting total price hrk
	   		double totalHrk= 0; double tecaj = 0; double totalEur = 0;
	    	for (OrderItem orderItem : orderItems) {
	    		//get product price
	    		 totalHrk += orderItem.getProduct().getPriceHrk();
	       	}
	    	order.setTotalPriceHrk(totalHrk);
	    	
	    	//client to HNB API to retrieve current exchante rates(HRK/EUR)
	    	//GET https://api.hnb.hr/tecajn/v1?valuta=EUR
	    	/*RestTemplate restTemplate = new RestTemplate();
	    	String url="https://api.hnb.hr/tecajn/v1?valuta=EUR";
	    	String resp = restTemplate.getForObject(url, String.class);*/
	    	
	    	
	    	/*String resp = webClientService.getHNBTecajSync();
	    	
	    	JsonParser springParser = JsonParserFactory.getJsonParser();
	    	List < Object > list = springParser.parseList(resp);
	    	Object obj = list.get(0);
	    	if (obj instanceof Map) {
	    	    Map < String, Object > map = (Map < String, Object > ) obj;
	    	    String tecajStr = (String)map.get("Srednji za devize");
	    	    tecaj = Double.parseDouble(tecajStr.replace(',','.'));
	    	    
	    	 }*/
	    	
	    	tecaj = webClientService.getTecaj();
	    	if (tecaj > 0) {
	    		totalEur = totalHrk/tecaj;
	    	    BigDecimal totb=new BigDecimal(totalEur).setScale(2,RoundingMode.UP);
	    	    totalEur=totb.doubleValue();
	    	}
	    	//set total_price_eur
	    	order.setTotalPriceEur(totalEur);
	    	//set orderStatus
	    	order.setStatus(Status.SUBMITTED);
	    	
	    	orderService.updateOrder(order.getId(), order);
	              
	        return ResponseEntity.ok(OrderDto.from(order));
	    }
	    
	    @DeleteMapping("/orders/{id}")
	    public Map<String, Boolean> deleteOrder(@PathVariable(value = "id") Long id)
	         throws ResourceNotFoundException {
	    	//find Order	    	
	    	 //Order  order = orderService.getOrder(id);
	    	 //delete old order items
		  	List<OrderItem> oldOrderItems = orderItemService.getOrderItemsByOrderId(id);
		   	for (OrderItem orderItem : oldOrderItems) {
		   		orderItemService.deleteOrderItem(orderItem.getId());
		   	}
		   	orderService.deleteOrder(id);
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
