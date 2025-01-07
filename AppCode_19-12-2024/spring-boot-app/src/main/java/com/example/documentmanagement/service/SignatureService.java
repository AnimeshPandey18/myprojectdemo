package com.example.documentmanagement.service;

import com.example.documentmanagement.model.Signature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class SignatureService {

    @Value("${udms.host}")
    private String udmsHost;

    @Value("${udms.timeout}")
    private int udmsTimeout;

    private final RestTemplate restTemplate;

    public SignatureService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<Signature> retrieveSignatureByDetailID(String detailID) {
        try {
            String url = String.format("%s/signatures/detail/%s", udmsHost, detailID);
            Signature signature = restTemplate.getForObject(url, Signature.class);
            return Optional.ofNullable(signature);
        } catch (HttpClientErrorException.NotFound e) {
            System.err.println("Detail ID not found: " + detailID);
            return Optional.empty();
        } catch (ResourceAccessException e) {
            System.err.println("Timeout occurred while retrieving signature by detail ID: " + detailID);
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("An error occurred while retrieving signature by detail ID: " + detailID);
            return Optional.empty();
        }
    }

    public List<Signature> retrieveSignaturesByCNAID(String cnaID) {
        try {
            String url = String.format("%s/signatures/cna/%s", udmsHost, cnaID);
            Signature[] signatures = restTemplate.getForObject(url, Signature[].class);
            return List.of(signatures);
        } catch (HttpClientErrorException.NotFound e) {
            System.err.println("CNA ID not found: " + cnaID);
            return List.of();
        } catch (ResourceAccessException e) {
            System.err.println("Timeout occurred while retrieving signatures by CNA ID: " + cnaID);
            return List.of();
        } catch (Exception e) {
            System.err.println("An error occurred while retrieving signatures by CNA ID: " + cnaID);
            return List.of();
        }
    }

    public List<String> verifySignatureDetails(String cnaID) {
        try {
            String url = String.format("%s/signatures/verify/%s", udmsHost, cnaID);
            Signature[] signatures = restTemplate.getForObject(url, Signature[].class);
            return convertToUserFriendlyFormat(signatures);
        } catch (HttpClientErrorException.NotFound e) {
            System.err.println("CNA ID not found for verification: " + cnaID);
            return List.of("CNA ID not found");
        } catch (ResourceAccessException e) {
            System.err.println("Timeout occurred while verifying signature details for CNA ID: " + cnaID);
            return List.of("Timeout occurred");
        } catch (Exception e) {
            System.err.println("An error occurred while verifying signature details for CNA ID: " + cnaID);
            return List.of("An error occurred");
        }
    }

    private List<String> convertToUserFriendlyFormat(Signature[] signatures) {
        // Convert each signature to a user-friendly format
        return List.of(signatures).stream()
                .map(signature -> String.format("Signer: %s, Date: %s, Context: %s",
                        signature.getSignerName(), signature.getSignDate(), signature.getContext()))
                .toList();
    }
}
