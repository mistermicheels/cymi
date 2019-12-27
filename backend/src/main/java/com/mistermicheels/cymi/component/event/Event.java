package com.mistermicheels.cymi.component.event;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.mistermicheels.cymi.common.FieldLengths;
import com.mistermicheels.cymi.common.error.InvalidRequestException;
import com.mistermicheels.cymi.component.group.Group;

@Entity
@Table(name = "event_definition")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(nullable = false, length = FieldLengths.DEFAULT_STRING_LENGTH)
    private String name;

    @Column(nullable = false, columnDefinition = "timestamp with time zone")
    private ZonedDateTime startTimestamp;

    @Column(nullable = false, columnDefinition = "timestamp with time zone")
    private ZonedDateTime endTimestamp;

    @Column(nullable = false, length = FieldLengths.DEFAULT_STRING_LENGTH)
    private String location;

    @Column(nullable = true, columnDefinition = "text")
    private String description;

    @OneToMany(mappedBy = "event")
    private Set<EventResponse> responses;

    Event() {
    }

    Event(Group group, EventBasicData basicData) {
        if (!basicData.getEndTimestamp().isAfter(basicData.getStartTimestamp())) {
            throw new InvalidRequestException("End of event must be after start of event");
        }

        this.group = group;
        this.name = basicData.getName();
        this.startTimestamp = basicData.getStartTimestamp();
        this.endTimestamp = basicData.getEndTimestamp();
        this.location = basicData.getLocation();
        this.description = basicData.getDescription().orElse(null);
    }

    public Long getId() {
        return this.id;
    }

    public Long getGroupId() {
        return this.group.getId();
    }

    public String getGroupName() {
        return this.group.getName();
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

    public Optional<EventResponse> getResponseForUserId(Long userId) {
        return this.responses.stream().filter(response -> response.getUserId() == userId).findAny();
    }

}
