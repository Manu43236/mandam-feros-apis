package com.api.feros.service;

import com.api.feros.entity.AppUser;
import com.api.feros.entity.Client;
import com.api.feros.repository.ClientRepository;
import com.api.feros.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<AppUser> getAllUsersWithSearchAndFilters(String search, String clientId, boolean activeOnly, Pageable pageable) {
        return userRepository.findAllWithSearchAndFilters(search, clientId, activeOnly, pageable);
    }

    public List<AppUser> getAllUsers() {
        return userRepository.findAllWithClient();
    }

    public List<AppUser> getUsersByClientId(String clientId) {
        return userRepository.findByClientId(clientId);
    }

    public List<AppUser> getActiveUsersByClientId(String clientId) {
        return userRepository.findByClientIdAndIsArchivedFalse(clientId);
    }

    public Optional<AppUser> getUserById(String id) {
        return userRepository.findByIdWithClient(id);
    }

    @Transactional
    public AppUser createUser(AppUser user) {
        Client client = clientRepository.findById(user.getClient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + user.getClient().getId()));

        if (userRepository.existsByEmailIgnoreCaseAndClientId(user.getEmail(), client.getId())) {
            throw new IllegalArgumentException("User with email '" + user.getEmail() + "' already exists for this client");
        }

        user.setClient(client);
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(user.getPassword()));
        } else {
            throw new IllegalArgumentException("Password is required");
        }
        user.setPassword(null);
        if (user.getPin() != null && !user.getPin().isEmpty()) {
            user.setPinHash(passwordEncoder.encode(user.getPin()));
        }
        user.setPin(null);
        AppUser saved = userRepository.save(user);
        return userRepository.findByIdWithClient(saved.getId()).orElse(saved);
    }

    @Transactional
    public AppUser updateUser(String id, AppUser userDetails) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        if (!user.getEmail().equalsIgnoreCase(userDetails.getEmail())) {
            if (userRepository.existsByEmailIgnoreCaseAndClientId(userDetails.getEmail(), user.getClient().getId())) {
                throw new IllegalArgumentException("User with email '" + userDetails.getEmail() + "' already exists for this client");
            }
        }

        Client client = clientRepository.findById(userDetails.getClient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + userDetails.getClient().getId()));

        user.setClient(client);
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(userDetails.getPassword()));
        }
        user.setPhone(userDetails.getPhone());
        user.setRole(userDetails.getRole());
        user.setStatus(userDetails.getStatus());
        if (userDetails.getPin() != null && !userDetails.getPin().isEmpty()) {
            user.setPinHash(passwordEncoder.encode(userDetails.getPin()));
        }
        user.setLicenseNumber(userDetails.getLicenseNumber());
        user.setLicenseExpiryDate(userDetails.getLicenseExpiryDate());
        user.setAssignedVehicleId(userDetails.getAssignedVehicleId());
        user.setProfilePhotoUrl(userDetails.getProfilePhotoUrl());
        user.setIsArchived(userDetails.getIsArchived());
        user.setUpdatedBy(userDetails.getUpdatedBy());

        AppUser saved = userRepository.save(user);
        return userRepository.findByIdWithClient(saved.getId()).orElse(saved);
    }

    @Transactional
    public void archiveUser(String id) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        user.setIsArchived(true);
        userRepository.save(user);
    }

    @Transactional
    public void unarchiveUser(String id) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        user.setIsArchived(false);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
