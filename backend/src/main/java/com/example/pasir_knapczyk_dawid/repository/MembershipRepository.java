package com.example.pasir_knapczyk_dawid.repository;

import com.example.pasir_knapczyk_dawid.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    List<Membership> findByGroupId(Long groupId);
}