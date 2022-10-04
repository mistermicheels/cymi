package com.mistermicheels.cymi.web.api.controller.events.input;

import java.time.ZonedDateTime;

import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

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

    @Nullable
    public String description;

}
