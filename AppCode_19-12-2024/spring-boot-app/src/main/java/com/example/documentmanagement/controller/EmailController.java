package com.example.documentmanagement.controller;

import com.example.documentmanagement.exception.EmailException;
import com.example.documentmanagement.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    private static final String UDMS_HOST = "http://udms.example.com";
    private static final int UDMS_TIMEOUT = 3; // seconds

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody Map<String, Object> emailData) {
        try {
            validateEmailFields(emailData);
            emailService.sendEmail(emailData);
            return ResponseEntity.ok("Email sent successfully.");
        } catch (EmailException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while sending the email.");
        }
    }

    private void validateEmailFields(Map<String, Object> emailData) throws EmailException {
        if (emailData.get("recipients") == null || ((String) emailData.get("recipients")).isEmpty()) {
            throw new EmailException("Recipient field is required.");
        }
        if (emailData.get("subject") == null || ((String) emailData.get("subject")).isEmpty()) {
            throw new EmailException("Subject field is required.");
        }
        if (emailData.get("body") == null || ((String) emailData.get("body")).isEmpty()) {
            throw new EmailException("Body field is required.");
        }
    }

    private String fetchSignatureDetails(String detailId) throws Exception {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(UDMS_TIMEOUT))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(UDMS_HOST + "/signatures/" + detailId))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new EmailException("Failed to retrieve signature details.");
        }
    }
}
