package com.mistermicheels.cymi.web.api.output;

import java.time.ZonedDateTime;

import org.springframework.lang.Nullable;

import com.mistermicheels.cymi.component.event.EventResponseStatus;
import com.mistermicheels.cymi.component.event.entity.Event;
import com.mistermicheels.cymi.component.event.entity.EventResponse;

public class ApiEvent {

    private final Long id;
    private final String name;
    private final ZonedDateTime startTimestamp;
    private final ZonedDateTime endTimestamp;
    private final String location;
    private final String description;
    private final Integer numberYesResponses;
    private final Integer numberNoResponses;
    private final Integer numberMaybeResponses;

    @Nullable
    private EventResponseStatus ownStatus;

    @Nullable
    private String ownComment;

    public ApiEvent(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.startTimestamp = event.getStartTimestamp();
        this.endTimestamp = event.getEndTimestamp();
        this.location = event.getLocation();
        this.description = event.getDescription().orElse(null);
        this.numberYesResponses = event.getNumberYesResponses();
        this.numberNoResponses = event.getNumberNoResponses();
        this.numberMaybeResponses = event.getNumberMaybeResponses();
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

    public Integer getNumberOtherYesResponses() {
        if (this.ownStatus == EventResponseStatus.Yes) {
            return this.numberYesResponses - 1;
        } else {
            return this.numberYesResponses;
        }
    }

    public Integer getNumberOtherNoResponses() {
        if (this.ownStatus == EventResponseStatus.No) {
            return this.numberNoResponses - 1;
        } else {
            return this.numberNoResponses;
        }
    }

    public Integer getNumberOtherMaybeResponses() {
        if (this.ownStatus == EventResponseStatus.Maybe) {
            return this.numberMaybeResponses - 1;
        } else {
            return this.numberMaybeResponses;
        }
    }

    public @Nullable EventResponseStatus getOwnStatus() {
        return this.ownStatus;
    }

    public @Nullable String getOwnComment() {
        return this.ownComment;
    }

}
