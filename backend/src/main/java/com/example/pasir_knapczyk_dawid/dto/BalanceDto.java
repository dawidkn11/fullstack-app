package com.example.pasir_knapczyk_dawid.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class BalanceDto {
    private double balance;
    private double totalIncome;
    private double totalExpense;

    public BalanceDto(double totalIncome, double totalExpense, double balance) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.balance = balance;
    }
}
