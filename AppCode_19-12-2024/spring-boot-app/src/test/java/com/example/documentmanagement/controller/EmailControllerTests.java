package com.example.documentmanagement.controller;

import com.example.documentmanagement.service.EmailService;
import com.example.documentmanagement.model.EmailRequest;
import com.example.documentmanagement.model.EmailResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class EmailControllerTests {

    @InjectMocks
    private EmailController emailController;

    @Mock
    private EmailService emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendEmailSuccess() {
        // Arrange
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setRecipient("test@example.com");
        emailRequest.setSubject("Test Subject");
        emailRequest.setBody("Test Body");

        EmailResponse emailResponse = new EmailResponse();
        emailResponse.setStatus("Success");

        when(emailService.sendEmail(any(EmailRequest.class))).thenReturn(emailResponse);

        // Act
        ResponseEntity<EmailResponse> responseEntity = emailController.sendEmail(emailRequest);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Success", responseEntity.getBody().getStatus());
    }

    @Test
    public void testSendEmailFailure() {
        // Arrange
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setRecipient("test@example.com");
        emailRequest.setSubject("Test Subject");
        emailRequest.setBody("Test Body");

        EmailResponse emailResponse = new EmailResponse();
        emailResponse.setStatus("Failure");

        when(emailService.sendEmail(any(EmailRequest.class))).thenReturn(emailResponse);

        // Act
        ResponseEntity<EmailResponse> responseEntity = emailController.sendEmail(emailRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Failure", responseEntity.getBody().getStatus());
    }

    @Test
    public void testSendEmailInvalidRecipient() {
        // Arrange
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setRecipient("invalid-email");
        emailRequest.setSubject("Test Subject");
        emailRequest.setBody("Test Body");

        // Act
        ResponseEntity<EmailResponse> responseEntity = emailController.sendEmail(emailRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
