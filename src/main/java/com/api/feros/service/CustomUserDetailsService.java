package com.api.feros.service;

import com.api.feros.entity.AppUser;
import com.api.feros.repository.UserRepository;
import com.api.feros.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username can be "email" or "email:clientId" for multi-tenant
        final String email;
        final String clientId;
        if (username.contains(":")) {
            String[] parts = username.split(":", 2);
            email = parts[0].trim();
            clientId = parts[1].trim();
        } else {
            email = username;
            clientId = null;
        }

        AppUser user;
        if (clientId != null && !clientId.isEmpty()) {
            user = userRepository.findByEmailIgnoreCaseAndClientId(email, clientId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email + " for client"));
        } else {
            user = userRepository.findByEmailIgnoreCase(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        }

        return UserPrincipal.create(user);
    }

    public UserDetails loadUserByEmailAndClientId(String email, String clientId) {
        AppUser user = userRepository.findByEmailIgnoreCaseAndClientId(email, clientId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email + " for client"));
        return UserPrincipal.create(user);
    }
}
