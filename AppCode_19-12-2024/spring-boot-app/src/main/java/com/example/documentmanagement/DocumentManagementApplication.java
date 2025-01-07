package com.example.documentmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import java.net.http.HttpClient;
import java.time.Duration;

@SpringBootApplication
public class DocumentManagementApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(DocumentManagementApplication.class, args);
        } catch (Exception e) {
            // Log the exception and handle it appropriately
            System.err.println("Application failed to start: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10)) // Set timeout as per UDMS_Timeout
                .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
