package com.example.pasir_knapczyk_dawid.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

    @NotBlank(message = "Email nie może być pusty")
    @Email(message = "Nieprawidłowy format email")
    private String email;

    @NotBlank(message = "Hasło nie może być puste")
    private String password;
}
