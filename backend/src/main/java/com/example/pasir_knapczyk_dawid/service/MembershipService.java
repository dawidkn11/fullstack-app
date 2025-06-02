package com.example.pasir_knapczyk_dawid.service;

import com.example.pasir_knapczyk_dawid.dto.MembershipDTO;
import com.example.pasir_knapczyk_dawid.model.Group;
import com.example.pasir_knapczyk_dawid.model.Membership;
import com.example.pasir_knapczyk_dawid.model.User;
import com.example.pasir_knapczyk_dawid.repository.GroupRepository;
import com.example.pasir_knapczyk_dawid.repository.MembershipRepository;
import com.example.pasir_knapczyk_dawid.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembershipService {
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public MembershipService(MembershipRepository membershipRepository, UserRepository userRepository, GroupRepository groupRepository) {
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    public List<Membership> getGroupMembers(Long groupId) {
        return membershipRepository.findByGroupId(groupId);
    }

    public Membership addMember(MembershipDTO membershipDTO) {
        User user = userRepository.findByEmail(membershipDTO.getUserEmail())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o emailu: " + membershipDTO.getUserEmail()));

        Group group = groupRepository.findById(membershipDTO.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono grupy o ID: " + membershipDTO.getGroupId()));

        //walidacja
        boolean alreadyMember = membershipRepository.findByGroupId(group.getId()).stream()
                .anyMatch(membership -> membership.getUser().getId().equals(user.getId()));

        if (alreadyMember) {
            throw new IllegalStateException("Użytkownik jest już członkiem tej grupy.");
        }

        Membership membership = new Membership();
        membership.setUser(user);
        membership.setGroup(group);
        return membershipRepository.save(membership);
    }

    public void removeMember(Long membershipId) {
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new EntityNotFoundException("Członkostwo nie istnieje"));

        User currentUser = getCurrentUser();
        User groupOwner = membership.getGroup().getOwner();

        if (!currentUser.getId().equals(groupOwner.getId())) {
            throw new SecurityException("Tylko właściciel grupy może usuwać członków.");
        }

        membershipRepository.deleteById(membershipId);
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika: " + email));
    }

}
