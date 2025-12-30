package com.ItCareerElevatorFifthExercise.exceptions;

public class MailingProcessException extends RuntimeException {

    public MailingProcessException(String message) {
        super(message);
    }

    public MailingProcessException(String message, Throwable cause) {
        super(message, cause);
    }
}
