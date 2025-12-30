package com.ItCareerElevatorFifthExercise.services.interfaces;

import com.ItCareerElevatorFifthExercise.DTOs.RegisterUserEmailDTO;
import com.ItCareerElevatorFifthExercise.DTOs.SendMailMessageDTO;

public interface MailService {

    void sendMessageToReceiverThroughEmail(SendMailMessageDTO sendMailMessageDTO);

    void sendRegistrationEmail(RegisterUserEmailDTO registerUserEmailDTO);
}
