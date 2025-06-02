package com.example.pasir_knapczyk_dawid.service;

import com.example.pasir_knapczyk_dawid.dto.BalanceDto;
import com.example.pasir_knapczyk_dawid.dto.TransactionDTO;
import com.example.pasir_knapczyk_dawid.model.Transaction;
import com.example.pasir_knapczyk_dawid.model.TransactionType;
import com.example.pasir_knapczyk_dawid.model.User;
import com.example.pasir_knapczyk_dawid.repository.TransactionRepository;
import com.example.pasir_knapczyk_dawid.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public List<Transaction> getAllTransactions() {
        User user = getCurrentUser();
        return transactionRepository.findAllByUser(user);
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Nie znaleziono transakcji o ID " + id));
    }

    public Transaction updateTransaction(Long id, TransactionDTO transactionDTO) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono transakcji o ID " + id));

        if (!transaction.getUser().getEmail().equals(getCurrentUser().getEmail())) {
            throw new SecurityException("Brak dostępu do edycji tej transakcji");
        }

        transaction.setAmount(transactionDTO.getAmount());
        transaction.setType(TransactionType.valueOf(String.valueOf(transactionDTO.getType())));
        transaction.setTags(transactionDTO.getTags());
        transaction.setNotes(transactionDTO.getNotes());

        return transactionRepository.save(transaction);
    }

    public Transaction createTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setType(TransactionType.valueOf(String.valueOf(transactionDTO.getType())));
        transaction.setTags(transactionDTO.getTags());
        transaction.setNotes(transactionDTO.getNotes());
        transaction.setUser(getCurrentUser());
        transaction.setTimestamp(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono transakcji o ID " + id));

        // Pobranie aktualnie zalogowanego użytkownika
        User currentUser = getCurrentUser();

        // Sprawdzenie, czy transakcja należy do użytkownika
        if (!transaction.getUser().equals(currentUser)) {
            throw new SecurityException("Nie masz uprawnień do usunięcia tej transakcji.");
        }

        transactionRepository.delete(transaction);
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono zalogowanego użytkownika"));
    }

    public BalanceDto getUserBalance(User user, Float days) {
        List<Transaction> userTransactions = transactionRepository.findByUser(user);

        if (days != null) {
            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days.longValue());
            userTransactions = userTransactions.stream()
                    .filter(transaction -> transaction.getTimestamp().isAfter(cutoffDate))
                    .toList();
        }

        return getBalanceDTO(userTransactions);
    }

    private BalanceDto getBalanceDTO(List<Transaction> userTransactions) {
        double income = userTransactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double expense = userTransactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();

        return new BalanceDto(income, expense, income - expense);
    }

//    public BalanceDto getUserBalance(User user) {
//        List<Transaction> userTransactions = transactionRepository.findAllByUser(user);
//
//        double income = userTransactions.stream()
//                .filter(t -> t.getType() == TransactionType.INCOME)
//                .mapToDouble(Transaction::getAmount)
//                .sum();
//
//        double expense = userTransactions.stream()
//                .filter(t -> t.getType() == TransactionType.EXPENSE)
//                .mapToDouble(Transaction::getAmount)
//                .sum();
//
//        return new BalanceDto(income, expense, income - expense);
//    }

}
