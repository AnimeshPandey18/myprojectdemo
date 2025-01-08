package com.example.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Emails")
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "to", nullable = false)
    private String to;

    @Column(name = "cc")
    private String cc;

    @Column(name = "bcc")
    private String bcc;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "message_body", nullable = false, columnDefinition = "TEXT")
    private String messageBody;

    @ElementCollection
    @CollectionTable(name = "email_documents", joinColumns = @JoinColumn(name = "email_id"))
    @Column(name = "document")
    private List<String> documentList;

    @Column(name = "sender_email", nullable = false)
    private String senderEmail;

    @Column(name = "date_sent", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSent;

    // Default constructor
    public Email() {
    }

    // Parameterized constructor
    public Email(String accountNumber, String to, String cc, String bcc, String subject, String messageBody, List<String> documentList, String senderEmail, Date dateSent) {
        this.accountNumber = accountNumber;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.subject = subject;
        this.messageBody = messageBody;
        this.documentList = documentList;
        this.senderEmail = senderEmail;
        this.dateSent = dateSent;
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

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public List<String> getDocumentList() {
        return documentList;
    }

    public void setDocumentList(List<String> documentList) {
        this.documentList = documentList;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    @Override
    public String toString() {
        return "Email{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", to='" + to + '\'' +
                ", cc='" + cc + '\'' +
                ", bcc='" + bcc + '\'' +
                ", subject='" + subject + '\'' +
                ", messageBody='" + messageBody + '\'' +
                ", documentList=" + documentList +
                ", senderEmail='" + senderEmail + '\'' +
                ", dateSent=" + dateSent +
                '}';
    }
}
