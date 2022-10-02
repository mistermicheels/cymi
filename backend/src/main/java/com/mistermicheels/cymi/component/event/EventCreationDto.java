package com.mistermicheels.cymi.component.event;

import java.time.ZonedDateTime;
import java.util.Optional;

public class EventCreationDto {

    private final String name;
    private final ZonedDateTime startTimestamp;
    private final ZonedDateTime endTimestamp;
    private final String location;
    private final String description;

    public EventCreationDto(String name, ZonedDateTime startTimestamp, ZonedDateTime endTimestamp,
            String location) {
        this(name, startTimestamp, endTimestamp, location, null);
    }

    public EventCreationDto(String name, ZonedDateTime startTimestamp, ZonedDateTime endTimestamp,
            String location, String description) {
        this.name = name;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.location = location;
        this.description = description;
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

    public Optional<String> getDescription() {
        return Optional.ofNullable(this.description);
    }

}
