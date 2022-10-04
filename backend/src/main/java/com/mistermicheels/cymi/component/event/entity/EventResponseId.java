package com.mistermicheels.cymi.component.event.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.lang.Nullable;

@SuppressWarnings("serial")
@Embeddable
public class EventResponseId implements Serializable {

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "user_id")
    private Long userId;

    EventResponseId() {
    }

    EventResponseId(Long eventId, Long userId) {
        this.eventId = eventId;
        this.userId = userId;
    }

    Long getEventId() {
        return this.eventId;
    }

    Long getUserId() {
        return this.userId;
    }

    // auto-generated equals and hashCode

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        EventResponseId other = (EventResponseId) obj;
        if (this.eventId == null) {
            if (other.eventId != null)
                return false;
        } else if (!this.eventId.equals(other.eventId))
            return false;
        if (this.userId == null) {
            if (other.userId != null)
                return false;
        } else if (!this.userId.equals(other.userId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.eventId == null) ? 0 : this.eventId.hashCode());
        result = prime * result + ((this.userId == null) ? 0 : this.userId.hashCode());
        return result;
    }

}
