package com.example.service;

import com.example.model.Email;
import com.example.model.Signature;
import com.example.udms.UDMSClient;
import com.example.udms.UDMSException;
import com.example.udms.UDMSResponse;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class DistributeService {

    @Autowired
    private UDMSClient udmsClient;

    @Autowired
    private JavaMailSender mailSender;

    public byte[] generatePDF(String userId, String documentData) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText(documentData);
            contentStream.endText();
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();

        return outputStream.toByteArray();
    }

    public Signature retrieveSignatureByDetailID(String detailId) throws UDMSException {
        UDMSResponse<Signature> response = udmsClient.getSignatureByDetailId(detailId);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw new UDMSException("Failed to retrieve signature for detail ID: " + detailId);
        }
    }

    public List<Signature> fetchSignatureDetailsByCNAID(String cnaId) throws UDMSException {
        UDMSResponse<List<Signature>> response = udmsClient.getSignaturesByCnaId(cnaId);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw new UDMSException("Failed to retrieve signature details for CNA ID: " + cnaId);
        }
    }

    public List<Signature> fetchAllSignaturesByCNAID(String cnaId) throws UDMSException {
        UDMSResponse<List<Signature>> response = udmsClient.getAllSignaturesByCnaId(cnaId);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw new UDMSException("Failed to retrieve all signatures for CNA ID: " + cnaId);
        }
    }

    public void sendEmail(Email email) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(email.getSenderEmail());
        helper.setTo(email.getTo().toArray(new String[0]));
        if (email.getCc() != null) {
            helper.setCc(email.getCc().toArray(new String[0]));
        }
        if (email.getBcc() != null) {
            helper.setBcc(email.getBcc().toArray(new String[0]));
        }
        helper.setSubject(email.getSubject());
        helper.setText(email.getMessageBody(), true);

        if (email.getDocumentList() != null) {
            for (String document : email.getDocumentList()) {
                // Assuming document is a path to the file
                helper.addAttachment(document, new File(document));
            }
        }

        mailSender.send(mimeMessage);
    }
}
