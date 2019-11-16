package com.mistermicheels.cymi.component.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mistermicheels.cymi.common.error.InvalidRequestException;

@Service
class PasswordService {

    private final PasswordEncoder passwordEncoder;

    PasswordService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String getSaltedPasswordHash(String password) {
        return this.passwordEncoder.encode(password);
    }

    public void checkPassword(String password, String saltedPasswordHash) {
        if (!this.passwordEncoder.matches(password, saltedPasswordHash)) {
            throw new InvalidRequestException("Invalid password");
        }
    }

}
