package com.ItCareerElevatorFifthExercise.listeners;

import com.ItCareerElevatorFifthExercise.DTOs.RegisterUserEmailDTO;
import com.ItCareerElevatorFifthExercise.services.interfaces.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendRegistrationEmailListener {

    private final MailService mailService;

    @KafkaListener(
            topics = "${app.kafka.topics.mail-regiser-user:mailRegisterUser}",
            groupId = "${spring.kafka.consumer.send-registration-email-group-id}",
            containerFactory = "registrationMessageKafkaListenerContainerFactory"
    )
    public void handleEmailRegistrationForward(RegisterUserEmailDTO registerUserEmailDTO) {
        log.info("---> Handling message in the Kafka topic.");

        if (
            // @formatter:off
                registerUserEmailDTO == null ||
                registerUserEmailDTO.getEmail() == null ||
                registerUserEmailDTO.getUsername() == null
            // @formatter:on
        ) {
            log.info("Invalid registerUserEmailDTO {}.", registerUserEmailDTO);
            return; // TODO: Should anything else happen?
        }

        log.info("Received data from Kafka: {}.", registerUserEmailDTO);

        mailService.sendRegistrationEmail(registerUserEmailDTO);
    }
}
