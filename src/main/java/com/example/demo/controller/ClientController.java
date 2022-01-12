package com.example.demo.controller;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import com.example.demo.webClient.WebClientService;


@RestController
@RequestMapping("/wshop")
public class ClientController {
		
		@Autowired
	    private WebClientService webClientService;
		
			
	  @GetMapping("/EUR")
	    public ResponseEntity<String> getTecaj()
	        {
	    	String tecaj = webClientService.getHNBTecajSync();
	        return ResponseEntity.ok().body(tecaj);
	    }
	  
	 

	    @ExceptionHandler(ConstraintViolationException.class)
	    @ResponseStatus(HttpStatus.BAD_REQUEST)
	    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
	      return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
	    }

}
