package com.mistermicheels.cymi.component.user;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

import org.springframework.data.domain.Persistable;

/**
 * Base entity for a token identifying a user. The token serves as ID.
 * 
 * We implement the Persistable interface to make JPA immediately INSERT new tokens instead of performing a SELECT first.
 */
@MappedSuperclass
class UserTokenBase implements Persistable<String>{

    @Id
    private String id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private ZonedDateTime expirationTimestamp;    
    
    private transient boolean persisted;

    UserTokenBase() {
    }

    UserTokenBase(String id, User user, ZonedDateTime expirationTimestamp) {
        this.id = id;
        this.user = user;
        this.expirationTimestamp = expirationTimestamp;
    }
    
    public String getId() {
        return this.id;
    }
    
    public User getUser() {
        return this.user;
    }
    
    public Long getUserId() {
        // this will work without retrieving the entire user
        // accessing other fields would cause the user to be fetched
        return this.user.getId();
    }
    
    public ZonedDateTime getExpirationTimestamp() {
        return this.expirationTimestamp;
    }
    
    public boolean hasExpired() {
        return ZonedDateTime.now().isAfter(this.expirationTimestamp);
    }

    @Override
    public boolean isNew() {
        return !this.persisted;
    }

    @PostLoad
    public void postLoad() {
        this.persisted = true;
    }

    @PostUpdate
    public void postUpdate() {
        this.persisted = true;
    }

    @PostPersist
    public void postPersist() {
        this.persisted = true;
    }

}
