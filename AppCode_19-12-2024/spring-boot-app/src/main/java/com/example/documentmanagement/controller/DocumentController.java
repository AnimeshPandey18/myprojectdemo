package com.example.documentmanagement.controller;

import com.example.documentmanagement.exception.DocumentNotFoundException;
import com.example.documentmanagement.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private static final Logger LOGGER = Logger.getLogger(DocumentController.class.getName());
    private static final String UDMS_HOST = "http://udms.example.com";
    private static final int UDMS_TIMEOUT = 5000;

    @Autowired
    private DocumentService documentService;

    @GetMapping("/hierarchicalData")
    public ResponseEntity<String> getHierarchicalData() {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(java.time.Duration.ofMillis(UDMS_TIMEOUT))
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(UDMS_HOST + "/hierarchicalData"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return ResponseEntity.ok(response.body());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve hierarchical data");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving hierarchical data", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving hierarchical data");
        }
    }

    @GetMapping("/distributorContact/{accountNumber}")
    public ResponseEntity<String> retrieveDistributorContactDetails(@PathVariable String accountNumber) {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(java.time.Duration.ofMillis(UDMS_TIMEOUT))
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(UDMS_HOST + "/distributorContact/" + accountNumber))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return ResponseEntity.ok(response.body());
            } else if (response.statusCode() == 404) {
                throw new DocumentNotFoundException("Distributor contact details not found for account number: " + accountNumber);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve distributor contact details");
            }
        } catch (DocumentNotFoundException e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving distributor contact details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving distributor contact details");
        }
    }

    @PostMapping("/manageDistribution")
    public ResponseEntity<String> manageDocumentDistribution(@RequestBody Object documentDetails) {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(java.time.Duration.ofMillis(UDMS_TIMEOUT))
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(UDMS_HOST + "/manageDistribution"))
                    .POST(HttpRequest.BodyPublishers.ofString(documentDetails.toString()))
                    .header("Content-Type", "application/json")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return ResponseEntity.ok("Document distribution managed successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to manage document distribution");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error managing document distribution", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error managing document distribution");
        }
    }
}
