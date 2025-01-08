package com.example;

import com.example.controller.DocumentController;
import com.example.service.DocumentService;
import com.example.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DocumentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DocumentService documentService;

    @InjectMocks
    private DocumentController documentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(documentController).build();
    }

    @Test
    public void testRetrieveDistributorContactDetails() throws Exception {
        Distributor distributor = new Distributor(1L, "12345", "Distributor Name", "123-456-7890", "distributor@example.com", "123 Distributor St");
        when(documentService.getDistributorContactDetails(anyString())).thenReturn(distributor);

        mockMvc.perform(get("/documents/distributor/12345"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"id\":1,\"accountNumber\":\"12345\",\"distributorName\":\"Distributor Name\",\"phoneNumber\":\"123-456-7890\",\"emailAddress\":\"distributor@example.com\",\"address\":\"123 Distributor St\"}"));
    }

    @Test
    public void testRetrieveDocumentGUID() throws Exception {
        Document document = new Document(1L, "12345", "doc-123", "guid-123", "sub-123", "Document Name", "PDF", "Active", "2023-01-01", "2023-01-02");
        when(documentService.getDocumentByIdentifier(anyString())).thenReturn(document);

        mockMvc.perform(get("/documents/guid/doc-123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"id\":1,\"accountNumber\":\"12345\",\"documentIdentifier\":\"doc-123\",\"guid\":\"guid-123\",\"submissionNumber\":\"sub-123\",\"documentName\":\"Document Name\",\"documentType\":\"PDF\",\"status\":\"Active\",\"dateCreated\":\"2023-01-01\",\"dateModified\":\"2023-01-02\"}"));
    }

    @Test
    public void testRetrieveAndViewAccountDocuments() throws Exception {
        List<Document> documents = Arrays.asList(
                new Document(1L, "12345", "doc-123", "guid-123", "sub-123", "Document Name", "PDF", "Active", "2023-01-01", "2023-01-02"),
                new Document(2L, "12345", "doc-124", "guid-124", "sub-124", "Document Name 2", "PDF", "Inactive", "2022-01-01", "2022-01-02")
        );
        when(documentService.getAccountDocuments(anyString(), anyString())).thenReturn(documents);

        mockMvc.perform(get("/documents/account/12345?years=2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"id\":1,\"accountNumber\":\"12345\",\"documentIdentifier\":\"doc-123\",\"guid\":\"guid-123\",\"submissionNumber\":\"sub-123\",\"documentName\":\"Document Name\",\"documentType\":\"PDF\",\"status\":\"Active\",\"dateCreated\":\"2023-01-01\",\"dateModified\":\"2023-01-02\"},{\"id\":2,\"accountNumber\":\"12345\",\"documentIdentifier\":\"doc-124\",\"guid\":\"guid-124\",\"submissionNumber\":\"sub-124\",\"documentName\":\"Document Name 2\",\"documentType\":\"PDF\",\"status\":\"Inactive\",\"dateCreated\":\"2022-01-01\",\"dateModified\":\"2022-01-02\"}]"));
    }

    @Test
    public void testGenerateAndDownloadPDF() throws Exception {
        byte[] pdfContent = "PDF content".getBytes();
        when(documentService.generatePDF(anyString(), anyString())).thenReturn(pdfContent);

        mockMvc.perform(post("/documents/generate-pdf")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":\"user-123\",\"documentData\":\"data\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(content().bytes(pdfContent));
    }

    @Test
    public void testRetrieveSignatureByDetailID() throws Exception {
        Signature signature = new Signature(1L, "detail-123", "cna-123", "Signer Name", "Signature Data", "2023-01-01", "Context");
        when(documentService.getSignatureByDetailID(anyString())).thenReturn(signature);

        mockMvc.perform(get("/documents/signature/detail-123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"id\":1,\"detailId\":\"detail-123\",\"cnaId\":\"cna-123\",\"signerName\":\"Signer Name\",\"signatureData\":\"Signature Data\",\"dateSigned\":\"2023-01-01\",\"context\":\"Context\"}"));
    }

    @Test
    public void testRetrieveAllSignaturesForCNAID() throws Exception {
        List<Signature> signatures = Arrays.asList(
                new Signature(1L, "detail-123", "cna-123", "Signer Name", "Signature Data", "2023-01-01", "Context"),
                new Signature(2L, "detail-124", "cna-123", "Signer Name 2", "Signature Data 2", "2023-01-02", "Context 2")
        );
        when(documentService.getAllSignaturesForCNAID(anyString())).thenReturn(signatures);

        mockMvc.perform(get("/documents/signatures/cna-123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"id\":1,\"detailId\":\"detail-123\",\"cnaId\":\"cna-123\",\"signerName\":\"Signer Name\",\"signatureData\":\"Signature Data\",\"dateSigned\":\"2023-01-01\",\"context\":\"Context\"},{\"id\":2,\"detailId\":\"detail-124\",\"cnaId\":\"cna-123\",\"signerName\":\"Signer Name 2\",\"signatureData\":\"Signature Data 2\",\"dateSigned\":\"2023-01-02\",\"context\":\"Context 2\"}]"));
    }

    @Test
    public void testSendEmail() throws Exception {
        mockMvc.perform(post("/documents/send-email")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"accountNumber\":\"12345\",\"to\":\"recipient@example.com\",\"cc\":\"cc@example.com\",\"bcc\":\"bcc@example.com\",\"subject\":\"Subject\",\"messageBody\":\"Message\",\"documentList\":[\"doc-123\",\"doc-124\"],\"senderEmail\":\"sender@example.com\"}"))
                .andExpect(status().isOk());
    }
}
