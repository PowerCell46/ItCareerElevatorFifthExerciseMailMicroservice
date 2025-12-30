package com.ItCareerElevatorFifthExercise.listeners;

import com.ItCareerElevatorFifthExercise.DTOs.SendMailMessageDTO;
import com.ItCareerElevatorFifthExercise.services.interfaces.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendEmailMessageListener {

    private final MailService mailService;

    @KafkaListener(
            topics = "${app.kafka.topics.mail-send-message:mailSendMessage}",
            groupId = "${spring.kafka.consumer.send-email-message-group-id}",
            containerFactory = "emailMessageKafkaListenerContainerFactory"
    )
    public void handleEmailMessageForward(SendMailMessageDTO sendMailMessageDTO) {
        log.info("---> Handling message in the Kafka topic.");

        if (
            // @formatter:off
                sendMailMessageDTO == null ||
                sendMailMessageDTO.getSenderUsername() == null ||
                sendMailMessageDTO.getReceiverEmail() == null ||
                sendMailMessageDTO.getContent() == null
            // @formatter:on
        ) {
            log.error("Invalid sendMailMessageDTO received: {}.", sendMailMessageDTO);
            return; // TODO: Should anything else happen?
        }

        log.info("Received data from Kafka: {}.", sendMailMessageDTO);

        // TODO: Implement Inbox pattern here

        mailService.sendMessageToReceiverThroughEmail(sendMailMessageDTO);
    }
}
