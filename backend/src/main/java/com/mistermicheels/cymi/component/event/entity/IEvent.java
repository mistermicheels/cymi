package com.mistermicheels.cymi.component.event.entity;

import java.time.ZonedDateTime;
import java.util.Optional;

public interface IEvent {

    Long getId();

    Long getGroupId();

    String getName();

    ZonedDateTime getStartTimestamp();

    ZonedDateTime getEndTimestamp();

    String getLocation();

    Optional<String> getDescription();

    Integer getNumberYesResponses();

    Integer getNumberNoResponses();

    Integer getNumberMaybeResponses();

}
