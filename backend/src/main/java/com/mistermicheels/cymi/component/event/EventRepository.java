package com.mistermicheels.cymi.component.event;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mistermicheels.cymi.component.event.entity.Event;

interface EventRepository extends JpaRepository<Event, Long> {

    // @formatter:off
    @Query("SELECT event "
            + "FROM Event event "
            + "JOIN FETCH event.group "
            + "WHERE event.id = :id")
    // @formatter:on
    Optional<Event> findWithGroupById(@Param(value = "id") Long id);

    // @formatter:off
    @Query("SELECT DISTINCT event "
            + "FROM Event event "
            + "WHERE event.group.id = :groupId "
            + "AND event.endTimestamp > CURRENT_TIMESTAMP "
            + "ORDER BY event.startTimestamp ASC")
    // @formatter:on
    List<Event> findUpcomingByGroupId(@Param(value = "groupId") Long groupId);

    // @formatter:off
    @Query("SELECT DISTINCT event "
            + "FROM Event event "
            + "JOIN FETCH event.group containingGroup "
            + "JOIN containingGroup.members membership "
            + "WHERE membership.groupUserLinkId.userId = :userId "
            + "AND event.endTimestamp > CURRENT_TIMESTAMP "
            + "ORDER BY event.startTimestamp ASC")
    // @formatter:on
    List<Event> findUpcomingWithGroupByUserId(@Param(value = "userId") Long userId);
}
