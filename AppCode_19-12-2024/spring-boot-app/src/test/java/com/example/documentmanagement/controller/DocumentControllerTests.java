package com.example.documentmanagement.controller;

import com.example.documentmanagement.service.DocumentService;
import com.example.documentmanagement.model.Document;
import com.example.documentmanagement.exception.DocumentNotFoundException;
import com.example.documentmanagement.exception.ExternalServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DocumentControllerTests {

    @Mock
    private DocumentService documentService;

    @InjectMocks
    private DocumentController documentController;

    private Document document;

    @BeforeEach
    public void setUp() {
        document = new Document();
        document.setDocumentId(1L);
        document.setTitle("Sample Document");
        document.setContent("This is a sample document.");
    }

    @Test
    public void testGetDocumentById_Success() {
        when(documentService.getDocumentById(anyLong())).thenReturn(Optional.of(document));

        ResponseEntity<Document> response = documentController.getDocumentById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(document, response.getBody());
    }

    @Test
    public void testGetDocumentById_NotFound() {
        when(documentService.getDocumentById(anyLong())).thenReturn(Optional.empty());

        try {
            documentController.getDocumentById(1L);
        } catch (DocumentNotFoundException e) {
            assertEquals("Document not found with id: 1", e.getMessage());
        }
    }

    @Test
    public void testGetDocumentById_ExternalServiceError() {
        when(documentService.getDocumentById(anyLong())).thenThrow(new ExternalServiceException("External service error"));

        try {
            documentController.getDocumentById(1L);
        } catch (ExternalServiceException e) {
            assertEquals("External service error", e.getMessage());
        }
    }

    // Additional test cases for other methods in DocumentController can be added here
}
