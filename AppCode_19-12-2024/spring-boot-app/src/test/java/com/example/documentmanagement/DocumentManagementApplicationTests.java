package com.example.documentmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class DocumentManagementApplicationTests {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DocumentManagementService documentManagementService;

    private static final String UDMS_HOST = "http://udms.example.com";
    private static final int UDMS_TIMEOUT = 5000;

    @BeforeEach
    public void setUp() {
        documentManagementService = new DocumentManagementService(restTemplate, UDMS_HOST, UDMS_TIMEOUT);
    }

    @Test
    public void testRetrieveDistributorContacts() {
        String distributorId = "123";
        String url = UDMS_HOST + "/distributors/" + distributorId + "/contacts";

        DistributorContact contact = new DistributorContact("John Doe", "john.doe@example.com", "123-456-7890");
        when(restTemplate.getForEntity(url, DistributorContact.class))
                .thenReturn(new ResponseEntity<>(contact, HttpStatus.OK));

        DistributorContact result = documentManagementService.retrieveDistributorContact(distributorId);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("123-456-7890", result.getPhoneNumber());
    }

    @Test
    public void testRetrieveDistributorContacts_NotFound() {
        String distributorId = "999";
        String url = UDMS_HOST + "/distributors/" + distributorId + "/contacts";

        when(restTemplate.getForEntity(url, DistributorContact.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Exception exception = assertThrows(HttpClientErrorException.class, () -> {
            documentManagementService.retrieveDistributorContact(distributorId);
        });

        assertEquals(HttpStatus.NOT_FOUND, ((HttpClientErrorException) exception).getStatusCode());
    }

    @Test
    public void testDocumentDistributionManagement() {
        String documentId = "doc123";
        String url = UDMS_HOST + "/documents/" + documentId + "/distribution";

        DocumentDistribution distribution = new DocumentDistribution(documentId, List.of("dist1", "dist2"));
        when(restTemplate.getForEntity(url, DocumentDistribution.class))
                .thenReturn(new ResponseEntity<>(distribution, HttpStatus.OK));

        DocumentDistribution result = documentManagementService.getDocumentDistribution(documentId);

        assertNotNull(result);
        assertEquals(documentId, result.getDocumentId());
        assertTrue(result.getDistributors().contains("dist1"));
        assertTrue(result.getDistributors().contains("dist2"));
    }

    @Test
    public void testExceptionHandling() {
        String url = UDMS_HOST + "/invalid-endpoint";

        when(restTemplate.getForEntity(url, String.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        Exception exception = assertThrows(HttpClientErrorException.class, () -> {
            documentManagementService.callInvalidEndpoint();
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ((HttpClientErrorException) exception).getStatusCode());
    }
}
