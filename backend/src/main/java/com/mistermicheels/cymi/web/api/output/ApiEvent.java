package com.mistermicheels.cymi.web.api.output;

import java.time.ZonedDateTime;

import com.mistermicheels.cymi.component.event.Event;
import com.mistermicheels.cymi.component.event.EventResponse;
import com.mistermicheels.cymi.component.event.EventResponseStatus;

public class ApiEvent {

    private final Long id;
    private final String name;
    private final ZonedDateTime startTimestamp;
    private final ZonedDateTime endTimestamp;
    private final String location;
    private final String description;

    private EventResponseStatus ownStatus;
    private String ownComment;

    public ApiEvent(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.startTimestamp = event.getStartTimestamp();
        this.endTimestamp = event.getEndTimestamp();
        this.location = event.getLocation();
        this.description = event.getDescription().orElse(null);
    }

    public void setOwnResponse(EventResponse response) {
        this.ownStatus = response.getStatus();
        this.ownComment = response.getComment().orElse(null);
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public ZonedDateTime getStartTimestamp() {
        return this.startTimestamp;
    }

    public ZonedDateTime getEndTimestamp() {
        return this.endTimestamp;
    }

    public String getLocation() {
        return this.location;
    }

    public String getDescription() {
        return this.description;
    }

    public EventResponseStatus getOwnStatus() {
        return this.ownStatus;
    }

    public String getOwnComment() {
        return this.ownComment;
    }

}
