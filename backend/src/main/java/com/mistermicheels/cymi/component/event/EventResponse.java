package com.mistermicheels.cymi.component.event;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.mistermicheels.cymi.common.FieldLengths;
import com.mistermicheels.cymi.component.user.User;

@Entity
@Table(name = "event_response")
public class EventResponse {

    @EmbeddedId
    private EventResponseId eventResponseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    @MapsId("eventId")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @MapsId("userId")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = FieldLengths.DEFAULT_STRING_LENGTH)
    private EventResponseStatus status;

    @Column(nullable = true, columnDefinition = "text")
    private String comment;

    public EventResponse() {
    }

    public EventResponse(Event event, User user, EventResponseStatus status) {
        this(event, user, status, null);
    }

    public EventResponse(Event event, User user, EventResponseStatus status, String comment) {
        this.eventResponseId = new EventResponseId(event.getId(), user.getId());
        this.event = event;
        this.user = user;
        this.status = status;
        this.comment = comment;
    }

    public Long getEventId() {
        return this.eventResponseId.getEventId();
    }

    public Long getUserId() {
        return this.eventResponseId.getUserId();
    }

    public EventResponseStatus getStatus() {
        return this.status;
    }

    public Optional<String> getComment() {
        return Optional.ofNullable(this.comment);
    }

}
