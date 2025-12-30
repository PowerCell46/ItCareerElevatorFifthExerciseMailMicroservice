package com.ItCareerElevatorFifthExercise.services.interfaces;

import com.ItCareerElevatorFifthExercise.DTOs.SendMailMessageDTO;

public interface MailService {

    void sendMessageToReceiverThroughEmail(SendMailMessageDTO sendMailMessageDTO);
}
