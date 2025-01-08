package com.example.model;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "Documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "document_identifier", nullable = false, unique = true)
    private String documentIdentifier;

    @Column(name = "guid", nullable = false, unique = true)
    private UUID guid;

    @Column(name = "submission_number", nullable = false)
    private String submissionNumber;

    @Column(name = "document_name", nullable = false)
    private String documentName;

    @Column(name = "document_type", nullable = false)
    private String documentType;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "date_created", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @Column(name = "date_modified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModified;

    // Default constructor
    public Document() {
    }

    // Parameterized constructor
    public Document(String accountNumber, String documentIdentifier, UUID guid, String submissionNumber, String documentName, String documentType, String status, Date dateCreated, Date dateModified) {
        this.accountNumber = accountNumber;
        this.documentIdentifier = documentIdentifier;
        this.guid = guid;
        this.submissionNumber = submissionNumber;
        this.documentName = documentName;
        this.documentType = documentType;
        this.status = status;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDocumentIdentifier() {
        return documentIdentifier;
    }

    public void setDocumentIdentifier(String documentIdentifier) {
        this.documentIdentifier = documentIdentifier;
    }

    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public String getSubmissionNumber() {
        return submissionNumber;
    }

    public void setSubmissionNumber(String submissionNumber) {
        this.submissionNumber = submissionNumber;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", documentIdentifier='" + documentIdentifier + '\'' +
                ", guid=" + guid +
                ", submissionNumber='" + submissionNumber + '\'' +
                ", documentName='" + documentName + '\'' +
                ", documentType='" + documentType + '\'' +
                ", status='" + status + '\'' +
                ", dateCreated=" + dateCreated +
                ", dateModified=" + dateModified +
                '}';
    }
}
