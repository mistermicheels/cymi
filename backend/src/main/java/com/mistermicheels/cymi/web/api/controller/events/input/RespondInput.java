package com.mistermicheels.cymi.web.api.controller.events.input;

import javax.validation.constraints.NotNull;

import com.mistermicheels.cymi.component.event.EventResponseStatus;

public class RespondInput {

    @NotNull
    public Long eventId;

    @NotNull
    public EventResponseStatus status;

    public String comment;

}
