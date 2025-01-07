package com.example.documentmanagement.service;

import com.example.documentmanagement.exception.SignatureNotFoundException;
import com.example.documentmanagement.model.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SignatureServiceTests {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SignatureService signatureService;

    private static final String UDMS_HOST = "http://udms.example.com";
    private static final int UDMS_TIMEOUT = 3000; // 3 seconds

    @BeforeEach
    public void setUp() {
        signatureService = new SignatureService(restTemplate, UDMS_HOST, UDMS_TIMEOUT);
    }

    @Test
    public void testRetrieveSignatureWithValidDetailId() {
        // Arrange
        String validDetailId = "validDetailId";
        Signature expectedSignature = new Signature("signatureId", "documentId", "John Doe", "2023-10-01", "context");
        when(restTemplate.getForObject(anyString(), eq(Signature.class)))
                .thenReturn(expectedSignature);

        // Act
        Instant start = Instant.now();
        Optional<Signature> actualSignature = signatureService.retrieveSignature(validDetailId);
        Instant end = Instant.now();

        // Assert
        assertTrue(actualSignature.isPresent());
        assertEquals(expectedSignature, actualSignature.get());
        assertTrue(Duration.between(start, end).toMillis() < UDMS_TIMEOUT);
    }

    @Test
    public void testRetrieveSignatureWithInvalidDetailId() {
        // Arrange
        String invalidDetailId = "invalidDetailId";
        when(restTemplate.getForObject(anyString(), eq(Signature.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Act & Assert
        assertThrows(SignatureNotFoundException.class, () -> {
            signatureService.retrieveSignature(invalidDetailId);
        });
    }

    @Test
    public void testRetrieveSignatureHandlesTimeout() {
        // Arrange
        String validDetailId = "validDetailId";
        when(restTemplate.getForObject(anyString(), eq(Signature.class)))
                .thenAnswer(invocation -> {
                    Thread.sleep(UDMS_TIMEOUT + 1000); // Simulate delay
                    return new Signature("signatureId", "documentId", "John Doe", "2023-10-01", "context");
                });

        // Act & Assert
        assertThrows(SignatureNotFoundException.class, () -> {
            signatureService.retrieveSignature(validDetailId);
        });
    }

    @Test
    public void testRetrieveSignatureHandlesHttpClientError() {
        // Arrange
        String detailId = "anyDetailId";
        when(restTemplate.getForObject(anyString(), eq(Signature.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        // Act & Assert
        assertThrows(SignatureNotFoundException.class, () -> {
            signatureService.retrieveSignature(detailId);
        });
    }
}
