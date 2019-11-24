package com.mistermicheels.cymi.component.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

interface SessionTokenRepository extends JpaRepository<SessionToken, String> {

    @Transactional
    @Modifying
    @Query("DELETE FROM SessionToken token WHERE CURRENT_TIMESTAMP > token.expirationTimestamp")
    void deleteExpired();

}
