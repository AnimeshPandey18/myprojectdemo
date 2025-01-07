package com.example.documentmanagement.controller;

import com.example.documentmanagement.service.SignatureService;
import com.example.documentmanagement.model.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SignatureControllerTests {

    @Mock
    private SignatureService signatureService;

    @InjectMocks
    private SignatureController signatureController;

    private Signature validSignature;
    private String validDetailId;
    private String invalidDetailId;

    @BeforeEach
    public void setUp() {
        validDetailId = "validDetailId";
        invalidDetailId = "invalidDetailId";
        validSignature = new Signature("signatureId", "documentId", "John Doe", "2023-10-01", "Underwriting");
    }

    @Test
    public void testRetrieveSignatureWithValidDetailId() throws Exception {
        when(signatureService.retrieveSignatureByDetailId(validDetailId)).thenReturn(Optional.of(validSignature));

        ResponseEntity<Signature> response = signatureController.getSignatureByDetailId(validDetailId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isPresent());
        assertEquals(validSignature, response.getBody().get());
    }

    @Test
    public void testRetrieveSignatureWithInvalidDetailId() throws Exception {
        when(signatureService.retrieveSignatureByDetailId(invalidDetailId)).thenReturn(Optional.empty());

        ResponseEntity<Signature> response = signatureController.getSignatureByDetailId(invalidDetailId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testRetrieveSignatureTimeout() throws Exception {
        when(signatureService.retrieveSignatureByDetailId(anyString())).thenThrow(new TimeoutException("Timeout occurred"));

        ResponseEntity<Signature> response = signatureController.getSignatureByDetailId(validDetailId);

        assertEquals(HttpStatus.REQUEST_TIMEOUT, response.getStatusCode());
    }

    @Test
    public void testRetrieveSignatureServiceException() throws Exception {
        when(signatureService.retrieveSignatureByDetailId(anyString())).thenThrow(new RuntimeException("Service exception"));

        ResponseEntity<Signature> response = signatureController.getSignatureByDetailId(validDetailId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
