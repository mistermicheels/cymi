package com.mistermicheels.cymi.component.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.mistermicheels.cymi.component.user.entity.EmailConfirmationToken;

interface EmailConfirmationTokenRepository extends JpaRepository<EmailConfirmationToken, String> {

    // @formatter:off
    @Query("SELECT token "
            + "FROM EmailConfirmationToken token "
            + "JOIN FETCH token.user "
            + "WHERE token.id = :id")
    // @formatter:on
    Optional<EmailConfirmationToken> findByIdWithUser(@Param(value = "id") String id);

    @Transactional
    @Modifying
    // @formatter:off
    @Query("DELETE FROM EmailConfirmationToken token "
            + "WHERE CURRENT_TIMESTAMP > token.expirationTimestamp")
    // @formatter:on
    void deleteExpired();

}
