package com.example.pasir_knapczyk_dawid.repository;

import com.example.pasir_knapczyk_dawid.model.Group;
import com.example.pasir_knapczyk_dawid.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByMembership_User(User user);
}
