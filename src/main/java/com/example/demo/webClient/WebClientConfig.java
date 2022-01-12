package com.example.demo.webClient;

//import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;


//"https://api.hnb.hr/tecajn/v1?valuta=EUR";

@Configuration
public class WebClientConfig {
 
  @Bean
  public WebClient todoWebClient(
    //@Value("${todo_base_url}") String todoBaseUrl, 
    WebClient.Builder webClientBuilder) {
    return webClientBuilder
      .baseUrl("https://api.hnb.hr/tecajn/v1?valuta=EUR")
      .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
      .build();
  }
}