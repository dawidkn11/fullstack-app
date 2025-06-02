package com.example.pasir_knapczyk_dawid.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "transactions")
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;


    @Enumerated(EnumType.STRING)
    private TransactionType type;


    private String tags;


    private String notes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    private LocalDateTime timestamp;

    public Transaction(Double amount, TransactionType type, String tags, String notes, User user) {
        this.amount = amount;
        this.type = type;
        this.tags = tags;
        this.notes = notes;
        this.user = user;
        this.timestamp = timestamp;
    }


}
