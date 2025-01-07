package com.example.documentmanagement.service;

import com.example.documentmanagement.model.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final HttpClient httpClient;

    @Value("${udms.host}")
    private String udmsHost;

    @Value("${udms.timeout}")
    private int udmsTimeout;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(java.time.Duration.ofSeconds(udmsTimeout))
                .build();
    }

    public void sendEmail(Email emailData) throws MessagingException {
        if (!validateEmail(emailData.getSenderId())) {
            throw new IllegalArgumentException("Invalid sender email address");
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(emailData.getSenderId());
        helper.setTo(emailData.getRecipient().split(","));
        if (StringUtils.hasText(emailData.getCc())) {
            helper.setCc(emailData.getCc().split(","));
        }
        if (StringUtils.hasText(emailData.getBcc())) {
            helper.setBcc(emailData.getBcc().split(","));
        }
        helper.setSubject(emailData.getSubject());
        helper.setText(emailData.getBody(), true);

        List<String> attachments = fetchEmailAttachments(emailData.getEmailId());
        for (String attachment : attachments) {
            // Assuming attachment is a file path or URL
            helper.addAttachment(attachment, new java.io.File(attachment));
        }

        mailSender.send(message);
    }

    public boolean validateEmail(String emailAddress) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(emailAddress).matches();
    }

    public List<String> fetchEmailAttachments(String emailID) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(udmsHost + "/emailAttachments?emailId=" + emailID))
                    .timeout(java.time.Duration.ofSeconds(udmsTimeout))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Assuming the response body contains a JSON array of attachment URLs
                return List.of(response.body().split(","));
            } else {
                throw new IOException("Failed to fetch email attachments, status code: " + response.statusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching email attachments", e);
        }
    }
}
