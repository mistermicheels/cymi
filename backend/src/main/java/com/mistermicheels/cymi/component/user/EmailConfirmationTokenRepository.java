package com.mistermicheels.cymi.component.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

interface EmailConfirmationTokenRepository extends JpaRepository<EmailConfirmationToken, String> {
    
    @Query("SELECT token FROM EmailConfirmationToken token JOIN FETCH token.user WHERE token.id = :id")
    Optional<EmailConfirmationToken> findByIdWithUser(@Param(value="id") String id);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM EmailConfirmationToken token WHERE CURRENT_TIMESTAMP > token.expirationTimestamp")
    void deleteExpired();

}
