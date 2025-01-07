package com.example.documentmanagement.service;

import com.example.documentmanagement.model.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DocumentService {

    private static final Logger LOGGER = Logger.getLogger(DocumentService.class.getName());

    @Value("${udms.host}")
    private String udmsHost;

    @Value("${udms.timeout}")
    private int udmsTimeout;

    private final RestTemplate restTemplate;

    public DocumentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String retrieveHierarchicalData() {
        try {
            URI uri = UriComponentsBuilder.fromHttpUrl(udmsHost + "/api/documents/hierarchical")
                    .build()
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving hierarchical data", e);
            throw new RuntimeException("Failed to retrieve hierarchical data", e);
        }
    }

    public String getDistributorContactDetails(String accountNumber) {
        try {
            URI uri = UriComponentsBuilder.fromHttpUrl(udmsHost + "/api/distributors/contact")
                    .queryParam("accountNumber", accountNumber)
                    .build()
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving distributor contact details for account number: " + accountNumber, e);
            throw new RuntimeException("Failed to retrieve distributor contact details", e);
        }
    }

    public void manageDocumentDistribution(Document documentDetails) {
        try {
            URI uri = UriComponentsBuilder.fromHttpUrl(udmsHost + "/api/documents/distribute")
                    .build()
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            HttpEntity<Document> entity = new HttpEntity<>(documentDetails, headers);

            restTemplate.exchange(uri, HttpMethod.POST, entity, Void.class);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error managing document distribution", e);
            throw new RuntimeException("Failed to manage document distribution", e);
        }
    }
}
