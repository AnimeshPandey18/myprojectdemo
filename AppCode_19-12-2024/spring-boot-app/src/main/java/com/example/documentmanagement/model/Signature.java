package com.example.documentmanagement.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Signatures")
public class Signature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "signature_id", nullable = false, updatable = false)
    private Long signatureId;

    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @Column(name = "signer_name", nullable = false, length = 100)
    private String signerName;

    @Column(name = "sign_date", nullable = false)
    private LocalDateTime signDate;

    @Column(name = "context", length = 255)
    private String context;

    // Default constructor
    public Signature() {
    }

    // Getters
    public Long getSignatureId() {
        return signatureId;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public String getSignerName() {
        return signerName;
    }

    public LocalDateTime getSignDate() {
        return signDate;
    }

    public String getContext() {
        return context;
    }

    // Setters
    public void setSignatureId(Long signatureId) {
        this.signatureId = signatureId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public void setSignerName(String signerName) {
        this.signerName = signerName;
    }

    public void setSignDate(LocalDateTime signDate) {
        this.signDate = signDate;
    }

    public void setContext(String context) {
        this.context = context;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "Signature{" +
                "signatureId=" + signatureId +
                ", documentId=" + documentId +
                ", signerName='" + signerName + '\'' +
                ", signDate=" + signDate +
                ", context='" + context + '\'' +
                '}';
    }
}
