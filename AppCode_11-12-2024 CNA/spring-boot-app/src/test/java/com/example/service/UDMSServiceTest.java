package com.example.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import com.example.model.DistributorContact;
import com.example.model.DocumentGUID;
import com.example.model.AccountDocuments;
import com.example.exception.UDMSException;

@ExtendWith(MockitoExtension.class)
public class UDMSServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UDMSService udmsService;

    private String baseUrl;
    private String distributorContactUrl;
    private String documentGuidUrl;
    private String accountDocumentsUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://udms.example.com";
        distributorContactUrl = baseUrl + "/distributor/contact";
        documentGuidUrl = baseUrl + "/document/guid";
        accountDocumentsUrl = baseUrl + "/account/documents";
    }

    @Test
    public void testRetrieveDistributorContactDetails_Success() {
        String accountNumber = "12345";
        DistributorContact mockContact = new DistributorContact("Distributor Name", "123-456-7890", "distributor@example.com", "123 Distributor St");

        when(restTemplate.getForObject(distributorContactUrl + "?accountNumber=" + accountNumber, DistributorContact.class))
                .thenReturn(mockContact);

        DistributorContact contact = udmsService.retrieveDistributorContactDetails(accountNumber);

        assertEquals("Distributor Name", contact.getName());
        assertEquals("123-456-7890", contact.getPhoneNumber());
        assertEquals("distributor@example.com", contact.getEmail());
        assertEquals("123 Distributor St", contact.getAddress());
    }

    @Test
    public void testRetrieveDistributorContactDetails_InvalidAccountNumber() {
        String invalidAccountNumber = "invalid";

        when(restTemplate.getForObject(distributorContactUrl + "?accountNumber=" + invalidAccountNumber, DistributorContact.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(UDMSException.class, () -> {
            udmsService.retrieveDistributorContactDetails(invalidAccountNumber);
        });
    }

    @Test
    public void testRetrieveDistributorContactDetails_ExternalServiceError() {
        String accountNumber = "12345";

        when(restTemplate.getForObject(distributorContactUrl + "?accountNumber=" + accountNumber, DistributorContact.class))
                .thenThrow(new ResourceAccessException("Service Unavailable"));

        assertThrows(UDMSException.class, () -> {
            udmsService.retrieveDistributorContactDetails(accountNumber);
        });
    }

    @Test
    public void testRetrieveDocumentGUID_Success() {
        String documentIdentifier = "doc-123";
        DocumentGUID mockGuid = new DocumentGUID("guid-123");

        when(restTemplate.getForObject(documentGuidUrl + "?documentIdentifier=" + documentIdentifier, DocumentGUID.class))
                .thenReturn(mockGuid);

        DocumentGUID guid = udmsService.retrieveDocumentGUID(documentIdentifier);

        assertEquals("guid-123", guid.getGuid());
    }

    @Test
    public void testRetrieveDocumentGUID_InvalidDocumentIdentifier() {
        String invalidDocumentIdentifier = "invalid-doc";

        when(restTemplate.getForObject(documentGuidUrl + "?documentIdentifier=" + invalidDocumentIdentifier, DocumentGUID.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(UDMSException.class, () -> {
            udmsService.retrieveDocumentGUID(invalidDocumentIdentifier);
        });
    }

    @Test
    public void testRetrieveAccountDocuments_Success() {
        String accountNumber = "12345";
        int effectiveYears = 5;
        AccountDocuments mockDocuments = new AccountDocuments(); // Assume this is populated with mock data

        when(restTemplate.getForObject(accountDocumentsUrl + "?accountNumber=" + accountNumber + "&effectiveYears=" + effectiveYears, AccountDocuments.class))
                .thenReturn(mockDocuments);

        AccountDocuments documents = udmsService.retrieveAccountDocuments(accountNumber, effectiveYears);

        // Add assertions to verify the documents
    }

    @Test
    public void testRetrieveAccountDocuments_InvalidAccountNumber() {
        String invalidAccountNumber = "invalid";
        int effectiveYears = 5;

        when(restTemplate.getForObject(accountDocumentsUrl + "?accountNumber=" + invalidAccountNumber + "&effectiveYears=" + effectiveYears, AccountDocuments.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(UDMSException.class, () -> {
            udmsService.retrieveAccountDocuments(invalidAccountNumber, effectiveYears);
        });
    }

    @Test
    public void testRetrieveAccountDocuments_ExternalServiceError() {
        String accountNumber = "12345";
        int effectiveYears = 5;

        when(restTemplate.getForObject(accountDocumentsUrl + "?accountNumber=" + accountNumber + "&effectiveYears=" + effectiveYears, AccountDocuments.class))
                .thenThrow(new ResourceAccessException("Service Unavailable"));

        assertThrows(UDMSException.class, () -> {
            udmsService.retrieveAccountDocuments(accountNumber, effectiveYears);
        });
    }
}
