package com.example.demo.webClient;

import java.util.List;
import java.util.Map;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.AllArgsConstructor;


@Service
	@AllArgsConstructor
	public class WebClientService {
	    private final WebClient webClient;

	    public String getHNBTecajSync() {
	        /*return webClient
	                .get()
	                .uri("https://api.hnb.hr/tecajn/v1?valuta=EUR")
	                .retrieve()
	                .onStatus(HttpStatus::is4xxClientError,
	                        error -> Mono.error(new RuntimeException("API not found")))
	                .onStatus(HttpStatus::is5xxServerError,
	                        error -> Mono.error(new RuntimeException("Server is not responding")))
	                .bodyToMono(String.class)
	                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)))
	                .block();*/
	    	return this.webClient
	    		      .get()
	    		      .uri("https://api.hnb.hr/tecajn/v1?valuta=EUR")
	    		      .retrieve()
	    		      .bodyToMono(String.class)
	    		      .block();
	        
	        
	    }
	    
	    public double getTecaj() {
	    	String resp = getHNBTecajSync();
	    	double tecaj = 0;
	    	JsonParser springParser = JsonParserFactory.getJsonParser();
	    	List < Object > list = springParser.parseList(resp);
	    	Object obj = list.get(0);
	    	if (obj instanceof Map) {
	    	    Map < String, Object > map = (Map < String, Object > ) obj;
	    	    String tecajStr = (String)map.get("Srednji za devize");
	    	    tecaj = Double.parseDouble(tecajStr.replace(',','.'));
	    	    
	    	 }
	    	return tecaj;
	    }
	}