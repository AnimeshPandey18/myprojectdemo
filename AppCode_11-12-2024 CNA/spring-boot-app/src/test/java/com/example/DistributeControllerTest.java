package com.example;

import com.example.controller.DistributeController;
import com.example.service.DistributeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DistributeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DistributeService distributeService;

    @InjectMocks
    private DistributeController distributeController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(distributeController).build();
    }

    @Test
    public void testGeneratePdfDocument() throws Exception {
        // Mocking the service response
        when(distributeService.generatePdfDocument(any())).thenReturn(ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(new byte[0]));

        // Performing the test
        mockMvc.perform(post("/distribute/generate-pdf")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"data\":\"sample data\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }

    @Test
    public void testRetrieveSignatureByDetailId() throws Exception {
        // Mocking the service response
        when(distributeService.retrieveSignatureByDetailId(any())).thenReturn(ResponseEntity.ok().body("Sample Signature Data"));

        // Performing the test
        mockMvc.perform(get("/distribute/signature/{detailId}", "validDetailId")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Sample Signature Data"));
    }

    @Test
    public void testRetrieveSignatureByInvalidDetailId() throws Exception {
        // Mocking the service response
        when(distributeService.retrieveSignatureByDetailId(any())).thenReturn(ResponseEntity.status(404).body("Detail ID not found"));

        // Performing the test
        mockMvc.perform(get("/distribute/signature/{detailId}", "invalidDetailId")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Detail ID not found"));
    }

    @Test
    public void testViewSignatureDetailsForCnaId() throws Exception {
        // Mocking the service response
        when(distributeService.viewSignatureDetailsForCnaId(any())).thenReturn(ResponseEntity.ok().body("Signature Details"));

        // Performing the test
        mockMvc.perform(get("/distribute/signature-details/{cnaId}", "validCnaId")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Signature Details"));
    }

    @Test
    public void testRetrieveAllSignaturesForCnaId() throws Exception {
        // Mocking the service response
        when(distributeService.retrieveAllSignaturesForCnaId(any())).thenReturn(ResponseEntity.ok().body("All Signatures Data"));

        // Performing the test
        mockMvc.perform(get("/distribute/all-signatures/{cnaId}", "validCnaId")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("All Signatures Data"));
    }

    @Test
    public void testSendEmail() throws Exception {
        // Mocking the service response
        when(distributeService.sendEmail(any())).thenReturn(ResponseEntity.ok().body("Email Sent"));

        // Performing the test
        mockMvc.perform(post("/distribute/send-email")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"to\":\"test@example.com\",\"subject\":\"Test Email\",\"body\":\"This is a test email.\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Email Sent"));
    }
}
