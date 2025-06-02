package com.example.pasir_knapczyk_dawid.dto;

import com.example.pasir_knapczyk_dawid.model.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class TransactionDTO {


    @NotNull(message = "Kwota nie może być pusta")
    @Min(value = 1, message = "Kwota musi być większa niż 0")
    private Double amount;

    @NotNull(message = "Typ transakcji jest wymagany i musi być wartością INCOME lub EXPENSE")
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Size(max = 50, message = "Tagi nie mogą przekraczać 50 znaków")
    private String tags;

    @Size(max = 255, message = "Notatka może mieć maksymalnie 255 znaków")
    private String notes;

    private LocalDateTime timestamp;




}
