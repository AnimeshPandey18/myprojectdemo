package com.example.documentmanagement.service;

import com.example.documentmanagement.model.Document;
import com.example.documentmanagement.model.Signature;
import com.example.documentmanagement.exception.DocumentNotFoundException;
import com.example.documentmanagement.exception.SignatureNotFoundException;
import com.example.documentmanagement.exception.UDMSException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DocumentServiceTests {

    @Mock
    private UDMSClient udmsClient;

    @InjectMocks
    private DocumentService documentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRetrieveDocumentSuccess() {
        // Arrange
        String documentId = "doc123";
        Document mockDocument = new Document(documentId, "Sample Title", "Sample Content");
        when(udmsClient.getDocument(documentId)).thenReturn(Optional.of(mockDocument));

        // Act
        Document document = documentService.retrieveDocument(documentId);

        // Assert
        assertNotNull(document);
        assertEquals("Sample Title", document.getTitle());
        verify(udmsClient, times(1)).getDocument(documentId);
    }

    @Test
    public void testRetrieveDocumentNotFound() {
        // Arrange
        String documentId = "doc123";
        when(udmsClient.getDocument(documentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DocumentNotFoundException.class, () -> {
            documentService.retrieveDocument(documentId);
        });
        verify(udmsClient, times(1)).getDocument(documentId);
    }

    @Test
    public void testRetrieveSignatureSuccess() {
        // Arrange
        String signatureId = "sig123";
        Signature mockSignature = new Signature(signatureId, "John Doe", "2023-10-01");
        when(udmsClient.getSignature(signatureId)).thenReturn(Optional.of(mockSignature));

        // Act
        Signature signature = documentService.retrieveSignature(signatureId);

        // Assert
        assertNotNull(signature);
        assertEquals("John Doe", signature.getSignerName());
        verify(udmsClient, times(1)).getSignature(signatureId);
    }

    @Test
    public void testRetrieveSignatureNotFound() {
        // Arrange
        String signatureId = "sig123";
        when(udmsClient.getSignature(signatureId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SignatureNotFoundException.class, () -> {
            documentService.retrieveSignature(signatureId);
        });
        verify(udmsClient, times(1)).getSignature(signatureId);
    }

    @Test
    public void testHandleUDMSException() {
        // Arrange
        String documentId = "doc123";
        when(udmsClient.getDocument(documentId)).thenThrow(new UDMSException("UDMS service error"));

        // Act & Assert
        UDMSException exception = assertThrows(UDMSException.class, () -> {
            documentService.retrieveDocument(documentId);
        });
        assertEquals("UDMS service error", exception.getMessage());
        verify(udmsClient, times(1)).getDocument(documentId);
    }
}
