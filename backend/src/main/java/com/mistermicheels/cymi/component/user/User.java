package com.mistermicheels.cymi.component.user;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_account")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean emailConfirmed;

    @Column(nullable = true)
    private String saltedPasswordHash;

    @Column(nullable = true)
    private String defaultDisplayName;

    User() {
    }

    User(String email) {
        this.email = email;
    }

    public void signup(String saltedPasswordHash, String defaultDisplayName) {
        this.saltedPasswordHash = saltedPasswordHash;
        this.defaultDisplayName = defaultDisplayName;
    }

    public void confirmEmail() {
        this.emailConfirmed = true;
    }

    public Long getId() {
        return this.id;
    }
    
    void setIdForTest(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public boolean isEmailConfirmed() {
        return this.emailConfirmed;
    }

    public Optional<String> getDefaultDisplayName() {
        return Optional.ofNullable(this.defaultDisplayName);
    }

    public Optional<String> getSaltedPasswordHash() {
        return Optional.ofNullable(this.saltedPasswordHash);
    }

}
