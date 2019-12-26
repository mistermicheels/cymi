package com.mistermicheels.cymi.web.api.output;

import java.time.ZonedDateTime;

import com.mistermicheels.cymi.component.event.Event;

public class ApiEvent {

    private final Long id;
    private final String name;
    private final ZonedDateTime startTimestamp;
    private final ZonedDateTime endTimestamp;
    private final String location;
    private final String description;

    public ApiEvent(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.startTimestamp = event.getStartTimestamp();
        this.endTimestamp = event.getEndTimestamp();
        this.location = event.getLocation();
        this.description = event.getDescription().orElse(null);
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

}
