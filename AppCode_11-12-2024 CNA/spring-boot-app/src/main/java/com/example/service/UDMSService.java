package com.example.service;

import com.example.model.Distributor;
import com.example.model.Document;
import com.example.model.HierarchicalData;
import com.example.model.PDFDocument;
import com.example.model.Signature;
import com.example.model.Email;
import com.example.exception.UDMSException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class UDMSService {

    @Value("${udms.service.url}")
    private String udmsServiceUrl;

    @Value("${udms.service.timeout}")
    private int udmsServiceTimeout;

    private final RestTemplate restTemplate;

    public UDMSService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Distributor fetchDistributorContactDetails(String accountNumber) throws UDMSException {
        try {
            String url = udmsServiceUrl + "/distributors?accountNumber=" + accountNumber;
            ResponseEntity<Distributor> response = restTemplate.getForEntity(url, Distributor.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new UDMSException("Error fetching distributor contact details: " + e.getMessage(), e);
        }
    }

    public String getDocumentGUID(String documentIdentifier) throws UDMSException {
        try {
            String url = udmsServiceUrl + "/documents/guid?documentIdentifier=" + documentIdentifier;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new UDMSException("Error fetching document GUID: " + e.getMessage(), e);
        }
    }

    public List<Document> getAccountDocuments(String accountNumber, int effectiveYears) throws UDMSException {
        try {
            String url = udmsServiceUrl + "/documents?accountNumber=" + accountNumber + "&effectiveYears=" + effectiveYears;
            ResponseEntity<List<Document>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Document>>() {});
            return response.getBody().stream()
                    .sorted((d1, d2) -> d1.getDateCreated().compareTo(d2.getDateCreated()))
                    .collect(Collectors.toList());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new UDMSException("Error fetching account documents: " + e.getMessage(), e);
        }
    }

    public List<HierarchicalData> getHierarchicalData() throws UDMSException {
        try {
            String url = udmsServiceUrl + "/hierarchicalData";
            ResponseEntity<List<HierarchicalData>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<HierarchicalData>>() {});
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new UDMSException("Error fetching hierarchical data: " + e.getMessage(), e);
        }
    }

    public byte[] generatePDF(PDFDocument pdfDocument) throws UDMSException {
        try {
            String url = udmsServiceUrl + "/pdf/generate";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<PDFDocument> request = new HttpEntity<>(pdfDocument, headers);
            ResponseEntity<byte[]> response = restTemplate.postForEntity(url, request, byte[].class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new UDMSException("Error generating PDF: " + e.getMessage(), e);
        }
    }

    public Signature retrieveSignatureByDetailID(String detailID) throws UDMSException {
        try {
            String url = udmsServiceUrl + "/signatures?detailID=" + detailID;
            ResponseEntity<Signature> response = restTemplate.getForEntity(url, Signature.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new UDMSException("Error retrieving signature by detail ID: " + e.getMessage(), e);
        }
    }

    public Signature retrieveSignatureDetailsByCNAID(String cnaID) throws UDMSException {
        try {
            String url = udmsServiceUrl + "/signatures/details?cnaID=" + cnaID;
            ResponseEntity<Signature> response = restTemplate.getForEntity(url, Signature.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new UDMSException("Error retrieving signature details by CNA ID: " + e.getMessage(), e);
        }
    }

    public List<Signature> retrieveAllSignaturesByCNAID(String cnaID) throws UDMSException {
        try {
            String url = udmsServiceUrl + "/signatures/all?cnaID=" + cnaID;
            ResponseEntity<List<Signature>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Signature>>() {});
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new UDMSException("Error retrieving all signatures by CNA ID: " + e.getMessage(), e);
        }
    }

    public void sendEmail(Email email) throws UDMSException {
        try {
            String url = udmsServiceUrl + "/emails/send";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Email> request = new HttpEntity<>(email, headers);
            restTemplate.postForEntity(url, request, Void.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new UDMSException("Error sending email: " + e.getMessage(), e);
        }
    }

    private <T> T makeUDMSCall(String url, HttpMethod method, Object request, Class<T> responseType) throws UDMSException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> entity = new HttpEntity<>(request, headers);
            ResponseEntity<T> response = restTemplate.exchange(url, method, entity, responseType);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new UDMSException("Error making UDMS call: " + e.getMessage(), e);
        }
    }
}
