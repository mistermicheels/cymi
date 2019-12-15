package com.mistermicheels.cymi.component.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
class PasswordService {

    private final PasswordEncoder passwordEncoder;

    PasswordService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String getSaltedPasswordHash(String password) {
        return this.passwordEncoder.encode(password);
    }

    public boolean isValidPassword(String password, String saltedPasswordHash) {
        return this.passwordEncoder.matches(password, saltedPasswordHash);
    }

}
