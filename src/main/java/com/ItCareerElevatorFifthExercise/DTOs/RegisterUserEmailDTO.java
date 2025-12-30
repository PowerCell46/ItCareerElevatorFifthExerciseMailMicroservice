package com.ItCareerElevatorFifthExercise.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class RegisterUserEmailDTO {

    private String username;

    private String email;
}
