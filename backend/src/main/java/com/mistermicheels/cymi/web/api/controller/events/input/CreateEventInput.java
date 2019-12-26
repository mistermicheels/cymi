package com.mistermicheels.cymi.web.api.controller.events.input;

import java.time.ZonedDateTime;

import javax.validation.constraints.NotNull;

public class CreateEventInput {

    @NotNull
    public Long groupId;

    @NotNull
    public String name;

    @NotNull
    public ZonedDateTime startTimestamp;

    @NotNull
    public ZonedDateTime endTimestamp;

    @NotNull
    public String location;

    public String description;

}
