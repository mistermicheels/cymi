package com.mistermicheels.cymi.component.user.entity;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.validator.routines.EmailValidator;

import com.mistermicheels.cymi.common.FieldLengths;
import com.mistermicheels.cymi.common.error.InvalidRequestException;

@Entity
@Table(name = "user_account")
public class User implements IUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = FieldLengths.DEFAULT_STRING_LENGTH)
    private String email;

    @Column(nullable = false)
    private boolean emailConfirmed;

    @Column(nullable = true, length = FieldLengths.DEFAULT_STRING_LENGTH)
    private String saltedPasswordHash;

    @Column(nullable = true, length = FieldLengths.DEFAULT_STRING_LENGTH)
    private String defaultDisplayName;

    public static User forTestWithId(Long id) {
        User user = new User();
        user.setIdForTest(id);
        return user;
    }

    User() {
    }

    public User(String email) {
        if (email.length() > FieldLengths.DEFAULT_STRING_LENGTH) {
            throw new InvalidRequestException("Email should not be longer than "
                    + FieldLengths.DEFAULT_STRING_LENGTH + " characters");
        } else if (!EmailValidator.getInstance().isValid(email)) {
            throw new InvalidRequestException("Please provide a valid email address");
        }

        this.email = email;
    }

    public void signup(String saltedPasswordHash, String defaultDisplayName) {
        this.saltedPasswordHash = saltedPasswordHash;

        if (defaultDisplayName.length() > FieldLengths.DEFAULT_STRING_LENGTH) {
            throw new InvalidRequestException("Default display name should not be longer than "
                    + FieldLengths.DEFAULT_STRING_LENGTH + " characters");
        }

        this.defaultDisplayName = defaultDisplayName;
    }

    public void confirmEmail() {
        this.emailConfirmed = true;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    public void setIdForTest(Long id) {
        this.id = id;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    public boolean isEmailConfirmed() {
        return this.emailConfirmed;
    }

    @Override
    public Optional<String> getDefaultDisplayName() {
        return Optional.ofNullable(this.defaultDisplayName);
    }

    public Optional<String> getSaltedPasswordHash() {
        return Optional.ofNullable(this.saltedPasswordHash);
    }

}
