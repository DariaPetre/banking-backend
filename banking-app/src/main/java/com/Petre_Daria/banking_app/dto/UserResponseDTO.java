package com.Petre_Daria.banking_app.dto;

import com.Petre_Daria.banking_app.model.Role;

import java.time.LocalDate;

public record UserResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        LocalDate birthDate,
        Role role

) { }
