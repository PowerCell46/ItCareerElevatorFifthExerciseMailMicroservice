package com.ItCareerElevatorFifthExercise.services.implementations;

import com.ItCareerElevatorFifthExercise.DTOs.RegisterUserEmailDTO;
import com.ItCareerElevatorFifthExercise.DTOs.SendMailMessageDTO;
import com.ItCareerElevatorFifthExercise.exceptions.MailingProcessException;
import com.ItCareerElevatorFifthExercise.services.interfaces.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendMessageToReceiverThroughEmail(SendMailMessageDTO mailMessageDTO) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(mailMessageDTO.getReceiverEmail());
            helper.setSubject(String.format(
                    "New message from %s - %s.",
                    mailMessageDTO.getSenderUsername(),
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"))
            ));

            helper.setText(formatMessageBody(mailMessageDTO.getContent(), mailMessageDTO.getSenderUsername(), mailMessageDTO.getSentAt()), true);

            log.info("---| Sending email message notification to {}.", mailMessageDTO.getReceiverEmail());
            mailSender.send(message);

        } catch (MessagingException ex) {
            log.warn("Exception occurred while constructing/sending the email.", ex);
            throw new MailingProcessException("Failed to send message email.", ex);
        }
    }

    @Override
    public void sendRegistrationEmail(RegisterUserEmailDTO registerUserEmailDTO) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(registerUserEmailDTO.getEmail());
            helper.setSubject("Welcome to PowerCell46's messenger – your account is ready!");
            helper.setText(formatWelcomeEmailBody(registerUserEmailDTO.getUsername(), registerUserEmailDTO.getEmail()), true);

            log.info("---| Sending registration confirmation email to {}.", registerUserEmailDTO.getEmail());
            mailSender.send(message);

        } catch (MessagingException ex) {
            log.warn("Exception occurred while constructing/sending the registration email.", ex);
            throw new MailingProcessException("Failed to send registration email.", ex);
        }
    }

    private String formatWelcomeEmailBody(String username, String email) {
        try {
            final String htmlTemplateContent = Files
                    .readString(Path.of("src/main/resources/templates/registerUserEmailTemplate.html"));

            return htmlTemplateContent
                    .replace("{{username}}", username)
                    .replace("{{username}}", username)
                    .replace("{{email}}", email);

        } catch (IOException e) {
            log.warn("Error reading the registerUserEmailTemplate.");
            throw new RuntimeException(e);
        }
    }

    private String formatMessageBody(String messageContent, String senderUsername, LocalDateTime sentAt) {
        String formattedDate = sentAt.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' HH:mm"));
        String messagePreview = messageContent.length() > 100 ? messageContent.substring(0, 100) + "..." : messageContent;

        try {
            // TODO: When deployed change [YOUR_APP_URL] to the actual URL (so you can forward directly to there)
            final String htmlTemplateContent = Files
                    .readString(Path.of("src/main/resources/templates/offlineUserMessageTemplate.html"));

            return htmlTemplateContent
                    .replace("{{sender}}", senderUsername)
                    .replace("{{preview}}", messagePreview)
                    .replace("{{sender}}", senderUsername)
                    .replace("{{date}}", formattedDate);

        } catch (IOException e) {
            log.warn("Error reading the offlineUserMessageTemplate.");
            throw new RuntimeException(e);
        }
    }
}
