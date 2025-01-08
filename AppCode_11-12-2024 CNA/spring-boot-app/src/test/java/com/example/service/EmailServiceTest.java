package com.example.service;

import com.example.model.Email;
import com.example.repository.EmailRepository;
import com.example.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private EmailRepository emailRepository;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendEmail_Success() throws MessagingException {
        // Arrange
        Email email = new Email();
        email.setTo("recipient@example.com");
        email.setCc("cc@example.com");
        email.setBcc("bcc@example.com");
        email.setSubject("Test Subject");
        email.setMessageBody("Test Message Body");
        email.setSenderEmail("sender@example.com");

        MimeMessage mimeMessage = mock(MimeMessage.class);
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act
        emailService.sendEmail(email);

        // Assert
        verify(mailSender, times(1)).send(mimeMessage);
        verify(emailRepository, times(1)).save(any(Email.class));
    }

    @Test
    public void testSendEmail_Failure() throws MessagingException {
        // Arrange
        Email email = new Email();
        email.setTo("recipient@example.com");
        email.setCc("cc@example.com");
        email.setBcc("bcc@example.com");
        email.setSubject("Test Subject");
        email.setMessageBody("Test Message Body");
        email.setSenderEmail("sender@example.com");

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new MessagingException("Failed to send email")).when(mailSender).send(mimeMessage);

        // Act & Assert
        Exception exception = assertThrows(MessagingException.class, () -> {
            emailService.sendEmail(email);
        });

        assertEquals("Failed to send email", exception.getMessage());
        verify(emailRepository, never()).save(any(Email.class));
    }

    @Test
    public void testComposeEmail() {
        // Arrange
        Email email = new Email();
        email.setTo("recipient@example.com");
        email.setCc("cc@example.com");
        email.setBcc("bcc@example.com");
        email.setSubject("Test Subject");
        email.setMessageBody("Test Message Body");
        email.setSenderEmail("sender@example.com");

        // Act
        Email composedEmail = emailService.composeEmail(email.getTo(), email.getCc(), email.getBcc(), email.getSubject(), email.getMessageBody(), email.getSenderEmail());

        // Assert
        assertNotNull(composedEmail);
        assertEquals(email.getTo(), composedEmail.getTo());
        assertEquals(email.getCc(), composedEmail.getCc());
        assertEquals(email.getBcc(), composedEmail.getBcc());
        assertEquals(email.getSubject(), composedEmail.getSubject());
        assertEquals(email.getMessageBody(), composedEmail.getMessageBody());
        assertEquals(email.getSenderEmail(), composedEmail.getSenderEmail());
    }
}
