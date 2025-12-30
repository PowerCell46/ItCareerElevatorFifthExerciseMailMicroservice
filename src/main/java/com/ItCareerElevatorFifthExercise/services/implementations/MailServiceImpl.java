package com.ItCareerElevatorFifthExercise.services.implementations;

import com.ItCareerElevatorFifthExercise.DTOs.RegisterUserEmailDTO;
import com.ItCareerElevatorFifthExercise.DTOs.SendMailMessageDTO;
import com.ItCareerElevatorFifthExercise.services.interfaces.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendMessageToReceiverThroughEmail(SendMailMessageDTO sendMailMessageDTO) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, false);

            helper.setTo(sendMailMessageDTO.getReceiverEmail());
            helper.setSubject(String.format("Unread message from %s.", sendMailMessageDTO.getSenderUsername()));
            helper.setText(formatMessageBody(sendMailMessageDTO.getContent(), sendMailMessageDTO.getSenderUsername()), false);

            log.info("Sending the email to {}.", sendMailMessageDTO.getReceiverEmail());
            mailSender.send(message);

        } catch (MessagingException ex) {
            log.warn("Exception occurred while constructing/sending the email.", ex);

//            throw new ErrorMailingPdfInvoiceException("Failed to send invoice email.", ex); // TODO: throw custom exception
        }
    }

    @Override
    public void sendRegistrationEmail(RegisterUserEmailDTO registerUserEmailDTO) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, false);

            helper.setTo(registerUserEmailDTO.getEmail());
            helper.setSubject("Welcome to PowerCell's messaging system!");
            helper.setText(String.format("html body of a welcomming mesage %s", registerUserEmailDTO.getUsername()));

            log.info("Sending the email to {}.", registerUserEmailDTO.getEmail());
            mailSender.send(message);

        } catch (MessagingException ex) {
            log.warn("Exception occurred while constructing/sending the email.", ex);

//            throw new ErrorMailingPdfInvoiceException("Failed to send invoice email.", ex); // TODO: throw custom exception
        }
    }

    private String formatMessageBody(String messageContent, String senderUsername) {
        // TODO: Add sent at...
        return String.format("Hello there.\nYou have an unread message from %s: '%s...'.", senderUsername, messageContent.substring(0, 10));
    }
}
