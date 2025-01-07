package com.example.documentmanagement.service;

import com.example.documentmanagement.model.Email;
import com.example.documentmanagement.model.EmailAttachment;
import com.example.documentmanagement.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmailServiceTests {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmailService emailService;

    private static final String UDMS_HOST = "http://udms.example.com";
    private static final int UDMS_TIMEOUT = 3000;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testComposeEmail_Success() {
        Email email = new Email("sender@example.com", "recipient@example.com", "cc@example.com", "bcc@example.com", "Subject", "Body");
        when(restTemplate.postForEntity(any(String.class), any(Email.class), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        assertDoesNotThrow(() -> emailService.composeEmail(email));
        verify(restTemplate, times(1)).postForEntity(eq(UDMS_HOST + "/emails"), eq(email), eq(Void.class));
    }

    @Test
    void testComposeEmail_Failure() {
        Email email = new Email("sender@example.com", "recipient@example.com", "cc@example.com", "bcc@example.com", "Subject", "Body");
        when(restTemplate.postForEntity(any(String.class), any(Email.class), eq(Void.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        Exception exception = assertThrows(HttpClientErrorException.class, () -> emailService.composeEmail(email));
        assertEquals(HttpStatus.BAD_REQUEST, ((HttpClientErrorException) exception).getStatusCode());
    }

    @Test
    void testRetrieveEmailAttachments_Success() {
        List<EmailAttachment> attachments = Collections.singletonList(new EmailAttachment(1, 1, "document.pdf"));
        when(restTemplate.getForObject(any(String.class), eq(List.class)))
                .thenReturn(attachments);

        List<EmailAttachment> result = emailService.retrieveEmailAttachments(1);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("document.pdf", result.get(0).getDocumentId());
    }

    @Test
    void testRetrieveEmailAttachments_Failure() {
        when(restTemplate.getForObject(any(String.class), eq(List.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Exception exception = assertThrows(HttpClientErrorException.class, () -> emailService.retrieveEmailAttachments(1));
        assertEquals(HttpStatus.NOT_FOUND, ((HttpClientErrorException) exception).getStatusCode());
    }

    @Test
    void testSendEmail_Success() {
        Email email = new Email("sender@example.com", "recipient@example.com", "cc@example.com", "bcc@example.com", "Subject", "Body");
        when(restTemplate.postForEntity(any(String.class), any(Email.class), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        assertDoesNotThrow(() -> emailService.sendEmail(email));
        verify(restTemplate, times(1)).postForEntity(eq(UDMS_HOST + "/sendEmail"), eq(email), eq(Void.class));
    }

    @Test
    void testSendEmail_Failure() {
        Email email = new Email("sender@example.com", "recipient@example.com", "cc@example.com", "bcc@example.com", "Subject", "Body");
        when(restTemplate.postForEntity(any(String.class), any(Email.class), eq(Void.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        Exception exception = assertThrows(HttpClientErrorException.class, () -> emailService.sendEmail(email));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ((HttpClientErrorException) exception).getStatusCode());
    }
}
