package com.example.service;

import com.example.model.*;
import com.example.repository.*;
import com.example.udms.UDMSClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DocumentServiceTest {

    @Mock
    private DistributorRepository distributorRepository;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private HierarchicalDataRepository hierarchicalDataRepository;

    @Mock
    private PDFDocumentRepository pdfDocumentRepository;

    @Mock
    private SignatureRepository signatureRepository;

    @Mock
    private EmailRepository emailRepository;

    @Mock
    private UDMSClient udmsClient;

    @InjectMocks
    private DocumentService documentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRetrieveDistributorContactDetails() {
        String accountNumber = "12345";
        Distributor distributor = new Distributor(1L, accountNumber, "Distributor Name", "123-456-7890", "distributor@example.com", "123 Street, City, Country");
        when(distributorRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(distributor));

        Distributor result = documentService.retrieveDistributorContactDetails(accountNumber);

        assertNotNull(result);
        assertEquals("Distributor Name", result.getDistributorName());
        assertEquals("123-456-7890", result.getPhoneNumber());
        assertEquals("distributor@example.com", result.getEmailAddress());
        assertEquals("123 Street, City, Country", result.getAddress());
    }

    @Test
    public void testRetrieveDocumentGUID() {
        String documentIdentifier = "doc123";
        Document document = new Document(1L, "12345", documentIdentifier, "guid-12345", "sub-12345", "Document Name", "Type", "Status", null, null);
        when(documentRepository.findByDocumentIdentifier(documentIdentifier)).thenReturn(Optional.of(document));

        String result = documentService.retrieveDocumentGUID(documentIdentifier);

        assertNotNull(result);
        assertEquals("guid-12345", result);
    }

    @Test
    public void testRetrieveAndViewAccountDocuments() {
        String accountNumber = "12345";
        List<Document> documents = Arrays.asList(
                new Document(1L, accountNumber, "doc1", "guid1", "sub1", "Document 1", "Type 1", "Status 1", null, null),
                new Document(2L, accountNumber, "doc2", "guid2", "sub2", "Document 2", "Type 2", "Status 2", null, null)
        );
        when(documentRepository.findByAccountNumber(accountNumber)).thenReturn(documents);

        List<Document> result = documentService.retrieveAndViewAccountDocuments(accountNumber);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testViewHierarchicalData() {
        List<HierarchicalData> hierarchicalData = Arrays.asList(
                new HierarchicalData(1L, null, "Node 1", "Active", true),
                new HierarchicalData(2L, 1L, "Node 1.1", "Active", false)
        );
        when(hierarchicalDataRepository.findAll()).thenReturn(hierarchicalData);

        List<HierarchicalData> result = documentService.viewHierarchicalData();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGenerateAndDownloadPDF() {
        String userId = "user123";
        PDFDocument pdfDocument = new PDFDocument(1L, userId, "document data", "generated pdf", null);
        when(pdfDocumentRepository.save(any(PDFDocument.class))).thenReturn(pdfDocument);

        PDFDocument result = documentService.generateAndDownloadPDF(userId, "document data");

        assertNotNull(result);
        assertEquals("generated pdf", result.getGeneratedPdf());
    }

    @Test
    public void testRetrieveSignatureByDetailID() {
        String detailId = "detail123";
        Signature signature = new Signature(1L, detailId, "cna123", "Signer Name", "signature data", null, "context");
        when(signatureRepository.findByDetailId(detailId)).thenReturn(Optional.of(signature));

        Signature result = documentService.retrieveSignatureByDetailID(detailId);

        assertNotNull(result);
        assertEquals("Signer Name", result.getSignerName());
        assertEquals("signature data", result.getSignatureData());
    }

    @Test
    public void testViewSignatureDetailsForCNAID() {
        String cnaId = "cna123";
        List<Signature> signatures = Arrays.asList(
                new Signature(1L, "detail1", cnaId, "Signer 1", "signature data 1", null, "context 1"),
                new Signature(2L, "detail2", cnaId, "Signer 2", "signature data 2", null, "context 2")
        );
        when(signatureRepository.findByCnaId(cnaId)).thenReturn(signatures);

        List<Signature> result = documentService.viewSignatureDetailsForCNAID(cnaId);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testRetrieveAllSignaturesForCNAID() {
        String cnaId = "cna123";
        List<Signature> signatures = Arrays.asList(
                new Signature(1L, "detail1", cnaId, "Signer 1", "signature data 1", null, "context 1"),
                new Signature(2L, "detail2", cnaId, "Signer 2", "signature data 2", null, "context 2")
        );
        when(signatureRepository.findByCnaId(cnaId)).thenReturn(signatures);

        List<Signature> result = documentService.retrieveAllSignaturesForCNAID(cnaId);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testComposeAndSendEmail() {
        Email email = new Email(1L, "12345", "to@example.com", "cc@example.com", "bcc@example.com", "Subject", "Message Body", "doc1,doc2", "sender@example.com", null);
        when(emailRepository.save(any(Email.class))).thenReturn(email);

        Email result = documentService.composeAndSendEmail(email);

        assertNotNull(result);
        assertEquals("to@example.com", result.getTo());
        assertEquals("Subject", result.getSubject());
    }
}
