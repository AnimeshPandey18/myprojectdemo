package com.example.documentmanagement.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents an Email in the document management system.
 * This class encapsulates the details of an email including sender, recipient, CC, BCC, subject, body, and sent timestamp.
 */
public class Email {

    private Long emailId;
    private Long senderId;
    private String recipient;
    private String cc;
    private String bcc;
    private String subject;
    private String body;
    private LocalDateTime sentAt;

    // Constructor
    public Email(Long emailId, Long senderId, String recipient, String cc, String bcc, String subject, String body, LocalDateTime sentAt) {
        this.emailId = emailId;
        this.senderId = senderId;
        this.recipient = recipient;
        this.cc = cc;
        this.bcc = bcc;
        this.subject = subject;
        this.body = body;
        this.sentAt = sentAt;
    }

    // Getters
    public Long getEmailId() {
        return emailId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getCc() {
        return cc;
    }

    public String getBcc() {
        return bcc;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    // Setters
    public void setEmailId(Long emailId) {
        this.emailId = emailId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    // Override equals and hashCode for proper comparison and hashing
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email)) return false;
        Email email = (Email) o;
        return Objects.equals(emailId, email.emailId) &&
                Objects.equals(senderId, email.senderId) &&
                Objects.equals(recipient, email.recipient) &&
                Objects.equals(cc, email.cc) &&
                Objects.equals(bcc, email.bcc) &&
                Objects.equals(subject, email.subject) &&
                Objects.equals(body, email.body) &&
                Objects.equals(sentAt, email.sentAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailId, senderId, recipient, cc, bcc, subject, body, sentAt);
    }

    // Override toString for better logging and debugging
    @Override
    public String toString() {
        return "Email{" +
                "emailId=" + emailId +
                ", senderId=" + senderId +
                ", recipient='" + recipient + '\'' +
                ", cc='" + cc + '\'' +
                ", bcc='" + bcc + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", sentAt=" + sentAt +
                '}';
    }
}
