package com.example.controller;

import com.example.model.Email;
import com.example.service.DistributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/distribute")
public class DistributeController {

    @Autowired
    private DistributeService distributeService;

    @PostMapping("/generate-pdf")
    public ResponseEntity<byte[]> generatePDF(@RequestBody Map<String, Object> requestData) {
        try {
            byte[] pdfData = distributeService.generatePDF(requestData);
            if (pdfData == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=document.pdf");
            return ResponseEntity.ok().headers(headers).body(pdfData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/retrieve-signature/{detailId}")
    public ResponseEntity<Map<String, Object>> retrieveSignatureByDetailID(@PathVariable String detailId) {
        try {
            Map<String, Object> signatureDetails = distributeService.retrieveSignatureByDetailID(detailId);
            if (signatureDetails == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(signatureDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/view-signature-details/{cnaId}")
    public ResponseEntity<List<Map<String, Object>>> fetchSignatureDetailsByCNAID(@PathVariable String cnaId) {
        try {
            List<Map<String, Object>> signatureDetails = distributeService.fetchSignatureDetailsByCNAID(cnaId);
            if (signatureDetails == null || signatureDetails.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            return ResponseEntity.ok(signatureDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/retrieve-all-signatures/{cnaId}")
    public ResponseEntity<List<Map<String, Object>>> fetchAllSignaturesByCNAID(@PathVariable String cnaId) {
        try {
            List<Map<String, Object>> signatures = distributeService.fetchAllSignaturesByCNAID(cnaId);
            if (signatures == null || signatures.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            return ResponseEntity.ok(signatures);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestBody Email email, @RequestParam("attachments") List<MultipartFile> attachments) {
        try {
            boolean isSent = distributeService.sendEmail(email, attachments);
            if (!isSent) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email");
            }
            return ResponseEntity.ok("Email sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while sending the email");
        }
    }
}
