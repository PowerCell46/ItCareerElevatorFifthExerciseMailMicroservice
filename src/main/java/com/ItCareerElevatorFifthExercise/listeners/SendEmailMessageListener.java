package com.ItCareerElevatorFifthExercise.listeners;

import com.ItCareerElevatorFifthExercise.DTOs.SendMailMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendEmailMessageListener {

    @KafkaListener(
            topics = "${app.kafka.topics.mail-send-message:mailSendMessage}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "userLocationKafkaListenerContainerFactory"
    )
    public void handleUserLocationMessage(SendMailMessageDTO sendMailMessageDTO) {

    }
}
