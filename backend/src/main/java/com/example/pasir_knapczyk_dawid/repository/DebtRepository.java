package com.example.pasir_knapczyk_dawid.repository;

import com.example.pasir_knapczyk_dawid.model.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long> {
    List<Debt> findByGroupId(Long groupId);
}