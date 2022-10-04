package com.mistermicheels.cymi.component.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.mistermicheels.cymi.component.user.entity.SessionToken;

interface SessionTokenRepository extends JpaRepository<SessionToken, String> {

    @Transactional
    @Modifying
    // @formatter:off
    @Query("DELETE FROM SessionToken token "
            + "WHERE CURRENT_TIMESTAMP > token.expirationTimestamp")
    // @formatter:on
    void deleteExpired();

}
