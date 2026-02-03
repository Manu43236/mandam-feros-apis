package com.api.feros.service;

import com.api.feros.dto.LoginRequest;
import com.api.feros.dto.LoginResponse;
import com.api.feros.entity.AppUser;
import com.api.feros.repository.UserRepository;
import com.api.feros.security.UserPrincipal;
import com.api.feros.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        UserPrincipal principal;
        if (request.isEmailPasswordLogin()) {
            AppUser user = userRepository.findByEmailIgnoreCaseWithClient(request.getEmail().trim())
                    .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
            if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
                throw new BadCredentialsException("Invalid email or password");
            }
            principal = UserPrincipal.create(user);
        } else if (request.isPhonePinLogin()) {
            AppUser user = userRepository.findByPhoneWithClient(request.getPhone().trim())
                    .orElseThrow(() -> new BadCredentialsException("Invalid phone or PIN"));
            if (user.getPinHash() == null || user.getPinHash().isEmpty()) {
                throw new BadCredentialsException("PIN not set for this user. Use email and password to login.");
            }
            if (!passwordEncoder.matches(request.getPin(), user.getPinHash())) {
                throw new BadCredentialsException("Invalid phone or PIN");
            }
            principal = UserPrincipal.create(user);
        } else {
            throw new BadCredentialsException("Either email+password or phone+PIN is required");
        }

        String accessToken = jwtUtil.generateAccessToken(
                principal.getId(),
                principal.getEmail(),
                principal.getRole(),
                principal.getClientId()
        );
        String refreshToken = jwtUtil.generateRefreshToken(
                principal.getId(),
                principal.getEmail(),
                principal.getClientId()
        );

        AppUser user = userRepository.findByIdWithClient(principal.getId()).orElseThrow();

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresInMs(jwtUtil.getExpirationMs())
                .tokenType("Bearer")
                .user(new LoginResponse.UserInfo(
                        user.getId(),
                        user.getEmail(),
                        user.getName(),
                        user.getRole(),
                        user.getClient().getId()
                ))
                .build();
    }

    public LoginResponse refresh(String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new BadCredentialsException("Refresh token is required");
        }
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new BadCredentialsException("Invalid or expired refresh token");
        }
        if (!"refresh".equals(jwtUtil.getTokenType(refreshToken))) {
            throw new BadCredentialsException("Invalid token type");
        }

        String userId = jwtUtil.getUserIdFromToken(refreshToken);
        String email = jwtUtil.getEmailFromToken(refreshToken);
        String clientId = jwtUtil.getTenantIdFromToken(refreshToken);

        AppUser user = userRepository.findByIdWithClient(userId)
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        String accessToken = jwtUtil.generateAccessToken(userId, email, user.getRole(), clientId);
        String newRefreshToken = jwtUtil.generateRefreshToken(userId, email, clientId);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .expiresInMs(jwtUtil.getExpirationMs())
                .tokenType("Bearer")
                .user(new LoginResponse.UserInfo(
                        user.getId(),
                        user.getEmail(),
                        user.getName(),
                        user.getRole(),
                        user.getClient().getId()
                ))
                .build();
    }
}
