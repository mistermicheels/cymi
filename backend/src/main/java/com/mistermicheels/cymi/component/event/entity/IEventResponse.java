package com.mistermicheels.cymi.component.event.entity;

import java.util.Optional;

import com.mistermicheels.cymi.component.event.EventResponseStatus;

public interface IEventResponse {

    Long getEventId();

    Long getUserId();

    EventResponseStatus getStatus();

    Optional<String> getComment();

}
