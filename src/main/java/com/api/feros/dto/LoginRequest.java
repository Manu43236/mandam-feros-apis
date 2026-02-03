package com.api.feros.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    /** Login with email + password */
    private String email;
    private String password;

    /** Login with phone + PIN */
    private String phone;
    private String pin;

    /** Either (email + password) or (phone + pin) must be provided. Client is determined by the user record (one user = one client). */
    public boolean isEmailPasswordLogin() {
        return email != null && !email.isBlank() && password != null && !password.isBlank();
    }

    public boolean isPhonePinLogin() {
        return phone != null && !phone.isBlank() && pin != null && !pin.isBlank();
    }
}
