package com.example.service;

import com.example.model.PDFDocument;
import com.example.model.Signature;
import com.example.model.Email;
import com.example.udms.UDMSClient;
import com.example.udms.UDMSException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DistributeServiceTest {

    @Mock
    private UDMSClient udmsClient;

    @InjectMocks
    private DistributeService distributeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerateAndDownloadPDF() {
        PDFDocument pdfDocument = new PDFDocument();
        pdfDocument.setGeneratedPdf(new byte[]{1, 2, 3});

        when(udmsClient.generatePDF(any(PDFDocument.class))).thenReturn(pdfDocument);

        PDFDocument result = distributeService.generateAndDownloadPDF(new PDFDocument());

        assertNotNull(result);
        assertArrayEquals(new byte[]{1, 2, 3}, result.getGeneratedPdf());
    }

    @Test
    public void testGenerateAndDownloadPDF_Error() {
        when(udmsClient.generatePDF(any(PDFDocument.class))).thenThrow(new UDMSException("Error generating PDF"));

        Exception exception = assertThrows(UDMSException.class, () -> {
            distributeService.generateAndDownloadPDF(new PDFDocument());
        });

        assertEquals("Error generating PDF", exception.getMessage());
    }

    @Test
    public void testRetrieveSignatureByDetailId_Valid() {
        Signature signature = new Signature();
        signature.setSignerName("John Doe");

        when(udmsClient.getSignatureByDetailId("validDetailId")).thenReturn(Optional.of(signature));

        Signature result = distributeService.retrieveSignatureByDetailId("validDetailId");

        assertNotNull(result);
        assertEquals("John Doe", result.getSignerName());
    }

    @Test
    public void testRetrieveSignatureByDetailId_Invalid() {
        when(udmsClient.getSignatureByDetailId("invalidDetailId")).thenReturn(Optional.empty());

        Exception exception = assertThrows(UDMSException.class, () -> {
            distributeService.retrieveSignatureByDetailId("invalidDetailId");
        });

        assertEquals("Detail ID not found", exception.getMessage());
    }

    @Test
    public void testViewSignatureDetailsForCnaId_Valid() {
        Signature signature = new Signature();
        signature.setSignerName("John Doe");

        when(udmsClient.getSignatureDetailsByCnaId("validCnaId")).thenReturn(Arrays.asList(signature));

        List<Signature> result = distributeService.viewSignatureDetailsForCnaId("validCnaId");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getSignerName());
    }

    @Test
    public void testViewSignatureDetailsForCnaId_Invalid() {
        when(udmsClient.getSignatureDetailsByCnaId("invalidCnaId")).thenReturn(Arrays.asList());

        List<Signature> result = distributeService.viewSignatureDetailsForCnaId("invalidCnaId");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testRetrieveAllSignaturesForCnaId_Valid() {
        Signature signature1 = new Signature();
        signature1.setSignerName("John Doe");
        Signature signature2 = new Signature();
        signature2.setSignerName("Jane Doe");

        when(udmsClient.getAllSignaturesByCnaId("validCnaId")).thenReturn(Arrays.asList(signature1, signature2));

        List<Signature> result = distributeService.retrieveAllSignaturesForCnaId("validCnaId");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getSignerName());
        assertEquals("Jane Doe", result.get(1).getSignerName());
    }

    @Test
    public void testRetrieveAllSignaturesForCnaId_Invalid() {
        when(udmsClient.getAllSignaturesByCnaId("invalidCnaId")).thenReturn(Arrays.asList());

        List<Signature> result = distributeService.retrieveAllSignaturesForCnaId("invalidCnaId");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testComposeAndSendEmail() {
        Email email = new Email();
        email.setTo("recipient@example.com");
        email.setSubject("Test Subject");
        email.setMessageBody("Test Body");

        when(udmsClient.sendEmail(any(Email.class))).thenReturn(true);

        boolean result = distributeService.composeAndSendEmail(email);

        assertTrue(result);
    }

    @Test
    public void testComposeAndSendEmail_Error() {
        Email email = new Email();
        email.setTo("recipient@example.com");
        email.setSubject("Test Subject");
        email.setMessageBody("Test Body");

        when(udmsClient.sendEmail(any(Email.class))).thenThrow(new UDMSException("Error sending email"));

        Exception exception = assertThrows(UDMSException.class, () -> {
            distributeService.composeAndSendEmail(email);
        });

        assertEquals("Error sending email", exception.getMessage());
    }
}
