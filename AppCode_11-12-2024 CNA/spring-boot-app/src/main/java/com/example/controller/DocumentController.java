package com.example.controller;

import com.example.model.Document;
import com.example.service.DocumentService;
import com.example.service.UDMSService;
import com.example.service.EmailService;
import com.example.service.PDFService;
import com.example.service.SignatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private UDMSService udmsService;

    @Autowired
    private PDFService pdfService;

    @Autowired
    private SignatureService signatureService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Map<String, String>> getDistributorContactDetails(@PathVariable String accountNumber) {
        try {
            Map<String, String> contactDetails = documentService.getDistributorContactDetails(accountNumber);
            return ResponseEntity.ok(contactDetails);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to retrieve distributor contact details."));
        }
    }

    @GetMapping("/udms-call")
    public ResponseEntity<String> makeUDMSCall() {
        try {
            String response = udmsService.makeUDMSCall();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to make UDMS call.");
        }
    }

    @GetMapping("/{documentIdentifier}/guid")
    public ResponseEntity<Map<String, String>> getDocumentGUID(@PathVariable String documentIdentifier) {
        try {
            Map<String, String> guidDetails = documentService.getDocumentGUID(documentIdentifier);
            return ResponseEntity.ok(guidDetails);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to retrieve document GUID."));
        }
    }

    @GetMapping("/{accountNumber}/view")
    public ResponseEntity<Map<String, List<Document>>> getAccountDocuments(@PathVariable String accountNumber) {
        try {
            Map<String, List<Document>> documents = documentService.getAccountDocuments(accountNumber);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to retrieve account documents."));
        }
    }

    @GetMapping("/hierarchical-data")
    public ResponseEntity<List<Map<String, Object>>> getHierarchicalData() {
        try {
            List<Map<String, Object>> hierarchicalData = documentService.getHierarchicalData();
            return ResponseEntity.ok(hierarchicalData);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(List.of(Map.of("error", "Failed to retrieve hierarchical data.")));
        }
    }

    @PostMapping("/generate-pdf")
    public ResponseEntity<byte[]> generatePDF(@RequestBody Map<String, Object> inputData) {
        try {
            byte[] pdfData = pdfService.generatePDF(inputData);
            return ResponseEntity.ok(pdfData);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/signatures/retrieve")
    public ResponseEntity<Map<String, String>> retrieveSignatureByDetailID(@RequestBody Map<String, String> request) {
        try {
            String detailID = request.get("detailID");
            Map<String, String> signatureDetails = signatureService.retrieveSignatureByDetailID(detailID);
            return ResponseEntity.ok(signatureDetails);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to retrieve signature by detail ID."));
        }
    }

    @GetMapping("/signatures/{cnaId}/details")
    public ResponseEntity<Map<String, String>> retrieveSignatureDetailsByCNAID(@PathVariable String cnaId) {
        try {
            Map<String, String> signatureDetails = signatureService.retrieveSignatureDetailsByCNAID(cnaId);
            return ResponseEntity.ok(signatureDetails);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to retrieve signature details by CNA ID."));
        }
    }

    @GetMapping("/signatures/{cnaId}/all")
    public ResponseEntity<List<Map<String, String>>> retrieveAllSignaturesByCNAID(@PathVariable String cnaId) {
        try {
            List<Map<String, String>> signatures = signatureService.retrieveAllSignaturesByCNAID(cnaId);
            return ResponseEntity.ok(signatures);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(List.of(Map.of("error", "Failed to retrieve all signatures by CNA ID.")));
        }
    }

    @PostMapping("/emails/send")
    public ResponseEntity<String> sendEmail(@RequestBody Map<String, Object> emailData) {
        try {
            emailService.sendEmail(emailData);
            return ResponseEntity.ok("Email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send email.");
        }
    }
}
