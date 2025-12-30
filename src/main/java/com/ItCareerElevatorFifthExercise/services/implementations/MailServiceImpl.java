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
                    "New message from %s - %s",
                    mailMessageDTO.getSenderUsername(),
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"))
            ));

            helper.setText(formatMessageBody(mailMessageDTO.getContent(), mailMessageDTO.getSenderUsername(), mailMessageDTO.getSentAt()), true);

            log.info("Sending email message notification to {}.", mailMessageDTO.getReceiverEmail());
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
            helper.setSubject("Welcome to PowerCell's messenger – your account is ready!");
            helper.setText(formatWelcomeEmailBody(registerUserEmailDTO.getUsername(), registerUserEmailDTO.getEmail()), true);

            log.info("Sending registration confirmation email to {}.", registerUserEmailDTO.getEmail());
            mailSender.send(message);

        } catch (MessagingException ex) {
            log.warn("Exception occurred while constructing/sending the registration email.", ex);
            throw new MailingProcessException("Failed to send registration email.", ex);
        }
    }

    // TODO: Move out the template to a different file and read it?
    private String formatWelcomeEmailBody(String username, String email) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <style>
                        body {
                            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
                            line-height: 1.6;
                            color: #333;
                            background-color: #f5f5f5;
                            margin: 0;
                            padding: 0;
                        }
                        .container {
                            max-width: 600px;
                            margin: 20px auto;
                            background-color: #ffffff;
                            border-radius: 8px;
                            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                            overflow: hidden;
                        }
                        .header {
                            background: linear-gradient(135deg, #208c99 0%, #1a7482 100%);
                            color: white;
                            padding: 40px 20px;
                            text-align: center;
                        }
                        .logo {
                            font-size: 28px;
                            font-weight: 700;
                            margin-bottom: 10px;
                            letter-spacing: 1px;
                        }
                        .tagline {
                            font-size: 14px;
                            opacity: 0.9;
                            margin: 0;
                        }
                        .content {
                            padding: 40px 20px;
                        }
                        .greeting {
                            font-size: 18px;
                            font-weight: 600;
                            color: #333;
                            margin-bottom: 15px;
                        }
                        .intro-text {
                            font-size: 15px;
                            color: #666;
                            margin-bottom: 25px;
                            line-height: 1.8;
                        }
                        .account-details {
                            background-color: #f9f9f9;
                            border-left: 4px solid #208c99;
                            padding: 20px;
                            margin: 30px 0;
                            border-radius: 4px;
                        }
                        .detail-row {
                            display: flex;
                            justify-content: space-between;
                            margin-bottom: 12px;
                            font-size: 14px;
                        }
                        .detail-label {
                            font-weight: 600;
                            color: #555;
                        }
                        .detail-value {
                            color: #208c99;
                            word-break: break-all;
                        }
                        .features-section {
                            margin: 30px 0;
                        }
                        .features-title {
                            font-size: 16px;
                            font-weight: 600;
                            color: #333;
                            margin-bottom: 15px;
                        }
                        .feature-list {
                            list-style: none;
                            padding: 0;
                            margin: 0;
                        }
                        .feature-item {
                            padding: 10px 0;
                            padding-left: 30px;
                            position: relative;
                            font-size: 14px;
                            color: #666;
                            line-height: 1.6;
                        }
                        .feature-item:before {
                            content: "✓";
                            position: absolute;
                            left: 0;
                            color: #208c99;
                            font-weight: 700;
                            font-size: 16px;
                        }
                        .cta-button {
                            display: inline-block;
                            margin-top: 25px;
                            padding: 14px 40px;
                            background-color: #208c99;
                            color: white;
                            text-decoration: none;
                            border-radius: 4px;
                            font-weight: 600;
                            font-size: 15px;
                            transition: background-color 0.3s ease;
                        }
                        .cta-button:hover {
                            background-color: #1a7482;
                        }
                        .security-note {
                            background-color: #f0f8f9;
                            border: 1px solid #d4e8eb;
                            padding: 15px;
                            margin: 25px 0;
                            border-radius: 4px;
                            font-size: 13px;
                            color: #555;
                        }
                        .security-note strong {
                            color: #208c99;
                        }
                        .footer {
                            background-color: #f5f5f5;
                            padding: 25px 20px;
                            text-align: center;
                            font-size: 12px;
                            color: #999;
                            border-top: 1px solid #e0e0e0;
                        }
                        .footer-links {
                            margin-bottom: 15px;
                        }
                        .footer-links a {
                            color: #208c99;
                            text-decoration: none;
                            margin: 0 10px;
                            font-size: 12px;
                        }
                        .footer-links a:hover {
                            text-decoration: underline;
                        }
                        .footer-text {
                            margin: 5px 0;
                            line-height: 1.6;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <div class="logo">⚡ PowerCell</div>
                            <p class="tagline">Real-time Messaging Platform</p>
                        </div>
                                
                        <div class="content">
                            <p class="greeting">Welcome to PowerCell's messenger, {{username}}! 🎉</p>
                                
                            <p class="intro-text">
                                Your account has been successfully created. We're excited to have you on board!
                                PowerCell's messenger brings seamless, real-time messaging to your fingertips. Get started
                                today and connect with others instantly.
                            </p>
                                
                            <div class="account-details">
                                <div class="detail-row">
                                    <span class="detail-label">Username: </span>
                                    <span class="detail-value">{{username}}</span>
                                </div>
                                <div class="detail-row">
                                    <span class="detail-label">Email: </span>
                                    <span class="detail-value">{{email}}</span>
                                </div>
                            </div>
                                
                            <div class="features-section">
                                <p class="features-title">What You Can Do With PowerCell:</p>
                                <ul class="feature-list">
                                    <li class="feature-item">Send and receive real-time messages instantly</li>
                                    <li class="feature-item">Create and manage multiple conversations</li>
                                    <li class="feature-item">Enjoy a clean, user-friendly interface</li>
                                    <li class="feature-item">Stay connected with friends and colleagues</li>
                                    <li class="feature-item">Access your messages anytime, anywhere</li>
                                </ul>
                            </div>
                                
                            <div class="security-note">
                                <strong>🔒 Security Tip:</strong> Never share your password with anyone.
                                PowerCell's messenger staff will never ask for your password via email. If you didn't create
                                this account, please contact us immediately.
                            </div>
                        </div>
                                
                        <div class="footer">
                            <p class="footer-text">
                                © 2025 PowerCell's messenger. All rights reserved.
                            </p>
                            <p class="footer-text">
                                This is an automated confirmation email. Please do not reply to this message.
                            </p>
                        </div>
                    </div>
                </body>
                </html>
                """
                .replace("{{username}}", username)
                .replace("{{username}}", username)
                .replace("{{email}}", email);
    }

    // TODO: Move out the template to a different file and read it?
    private String formatMessageBody(String messageContent, String senderUsername, LocalDateTime sentAt) {
        String formattedDate = sentAt.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' HH:mm"));
        String messagePreview = messageContent.length() > 100 ? messageContent.substring(0, 100) + "..." : messageContent;

        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <style>
                        body {
                            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
                            line-height: 1.6;
                            color: #333;
                            background-color: #f5f5f5;
                            margin: 0;
                            padding: 0;
                        }
                        .container {
                            max-width: 600px;
                            margin: 20px auto;
                            background-color: #ffffff;
                            border-radius: 8px;
                            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                            overflow: hidden;
                        }
                        .header {
                            background: linear-gradient(135deg, #208c99 0%, #1a7482 100%);
                            color: white;
                            padding: 30px 20px;
                            text-align: center;
                        }
                        .header h1 {
                            margin: 0;
                            font-size: 24px;
                            font-weight: 600;
                        }
                        .content {
                            padding: 30px 20px;
                        }
                        .greeting {
                            font-size: 16px;
                            margin-bottom: 20px;
                            color: #555;
                        }
                        .message-section {
                            background-color: #f9f9f9;
                            border-left: 4px solid #208c99;
                            padding: 15px;
                            margin: 20px 0;
                            border-radius: 4px;
                        }
                        .message-preview {
                            font-size: 14px;
                            color: #666;
                            font-style: italic;
                            margin: 10px 0;
                        }
                        .sender-info {
                            font-size: 13px;
                            color: #999;
                            margin-top: 15px;
                        }
                        .sender-name {
                            font-weight: 600;
                            color: #208c99;
                        }
                        .timestamp {
                            font-size: 12px;
                            color: #aaa;
                        }
                        .cta-button {
                            display: inline-block;
                            margin-top: 20px;
                            padding: 12px 30px;
                            background-color: #208c99;
                            color: white;
                            text-decoration: none;
                            border-radius: 4px;
                            font-weight: 500;
                            font-size: 14px;
                        }
                        .cta-button:hover {
                            background-color: #1a7482;
                        }
                        .footer {
                            background-color: #f5f5f5;
                            padding: 20px;
                            text-align: center;
                            font-size: 12px;
                            color: #999;
                            border-top: 1px solid #e0e0e0;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>📬 New Message</h1>
                        </div>
                
                        <div class="content">
                            <p class="greeting">Hello there,</p>
                
                            <p style="color: #666;">You have received a new message from <span class="sender-name">{{sender}}</span>:</p>
                
                            <div class="message-section">
                                <p class="message-preview">"{{preview}}"</p>
                            </div>
                
                            <div class="sender-info">
                                <p><strong>From:</strong> <span class="sender-name">{{sender}}</span></p>
                                <p class="timestamp"><strong>Sent:</strong> {{date}}</p>
                            </div>
                
                            <a href="[YOUR_APP_URL]/messages" class="cta-button">View Full Message</a>
                        </div>
                
                        <div class="footer">
                            <p>This is an automated notification. Please do not reply to this email.</p>
                            <p>&copy; 2025 Your Application. All rights reserved.</p>
                        </div>
                    </div>
                </body>
                </html>
                """ // ! When deployed change [YOUR_APP_URL]
                .replace("{{sender}}", senderUsername)
                .replace("{{preview}}", messagePreview)
                .replace("{{sender}}", senderUsername)
                .replace("{{date}}", formattedDate);
    }
}
