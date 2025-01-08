package com.example.service;

import com.example.model.Document;
import com.example.model.DistributorContact;
import com.example.model.Signature;
import com.example.model.Email;
import com.example.udms.UDMSClient;
import com.example.udms.UDMSException;
import com.example.udms.UDMSResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    @Autowired
    private UDMSClient udmsClient;

    public DistributorContact getDistributorContactDetails(String accountNumber) {
        try {
            UDMSResponse<DistributorContact> response = udmsClient.getDistributorContactDetails(accountNumber);
            if (response.isSuccessful()) {
                return response.getData();
            } else {
                throw new RuntimeException("Failed to retrieve distributor contact details: " + response.getErrorMessage());
            }
        } catch (UDMSException e) {
            throw new RuntimeException("Error while fetching distributor contact details", e);
        }
    }

    public String getDocumentGUID(String documentIdentifier) {
        try {
            UDMSResponse<String> response = udmsClient.getDocumentGUID(documentIdentifier);
            if (response.isSuccessful()) {
                return response.getData();
            } else {
                throw new RuntimeException("Failed to retrieve document GUID: " + response.getErrorMessage());
            }
        } catch (UDMSException e) {
            throw new RuntimeException("Error while fetching document GUID", e);
        }
    }

    public List<Document> getAccountDocuments(String accountNumber) {
        try {
            UDMSResponse<List<Document>> response = udmsClient.getAccountDocuments(accountNumber);
            if (response.isSuccessful()) {
                return response.getData();
            } else {
                throw new RuntimeException("Failed to retrieve account documents: " + response.getErrorMessage());
            }
        } catch (UDMSException e) {
            throw new RuntimeException("Error while fetching account documents", e);
        }
    }

    public List<HierarchicalData> getHierarchicalData() {
        try {
            UDMSResponse<List<HierarchicalData>> response = udmsClient.getHierarchicalData();
            if (response.isSuccessful()) {
                return response.getData();
            } else {
                throw new RuntimeException("Failed to retrieve hierarchical data: " + response.getErrorMessage());
            }
        } catch (UDMSException e) {
            throw new RuntimeException("Error while fetching hierarchical data", e);
        }
    }

    public byte[] generatePDF(String userId, String documentData) {
        try {
            UDMSResponse<byte[]> response = udmsClient.generatePDF(userId, documentData);
            if (response.isSuccessful()) {
                return response.getData();
            } else {
                throw new RuntimeException("Failed to generate PDF: " + response.getErrorMessage());
            }
        } catch (UDMSException e) {
            throw new RuntimeException("Error while generating PDF", e);
        }
    }

    public Signature retrieveSignatureByDetailID(String detailID) {
        try {
            UDMSResponse<Signature> response = udmsClient.retrieveSignatureByDetailID(detailID);
            if (response.isSuccessful()) {
                return response.getData();
            } else {
                throw new RuntimeException("Failed to retrieve signature by detail ID: " + response.getErrorMessage());
            }
        } catch (UDMSException e) {
            throw new RuntimeException("Error while fetching signature by detail ID", e);
        }
    }

    public List<Signature> retrieveSignatureDetailsByCNAID(String cnaID) {
        try {
            UDMSResponse<List<Signature>> response = udmsClient.retrieveSignatureDetailsByCNAID(cnaID);
            if (response.isSuccessful()) {
                return response.getData();
            } else {
                throw new RuntimeException("Failed to retrieve signature details by CNA ID: " + response.getErrorMessage());
            }
        } catch (UDMSException e) {
            throw new RuntimeException("Error while fetching signature details by CNA ID", e);
        }
    }

    public List<Signature> retrieveAllSignaturesByCNAID(String cnaID) {
        try {
            UDMSResponse<List<Signature>> response = udmsClient.retrieveAllSignaturesByCNAID(cnaID);
            if (response.isSuccessful()) {
                return response.getData();
            } else {
                throw new RuntimeException("Failed to retrieve all signatures by CNA ID: " + response.getErrorMessage());
            }
        } catch (UDMSException e) {
            throw new RuntimeException("Error while fetching all signatures by CNA ID", e);
        }
    }

    public void sendEmail(Email email) {
        try {
            UDMSResponse<Void> response = udmsClient.sendEmail(email);
            if (response.isSuccessful()) {
                // Email sent successfully
            } else {
                throw new RuntimeException("Failed to send email: " + response.getErrorMessage());
            }
        } catch (UDMSException e) {
            throw new RuntimeException("Error while sending email", e);
        }
    }
}
