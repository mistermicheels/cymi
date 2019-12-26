package com.mistermicheels.cymi.component.event;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT event FROM Event event JOIN FETCH event.group WHERE event.id = :id")
    Optional<Event> findWithGroupById(@Param(value = "id") Long id);

    @Query("SELECT event FROM Event event WHERE event.group.id = :groupId AND event.endTimestamp > CURRENT_TIMESTAMP ORDER BY event.startTimestamp ASC")
    List<Event> findUpcomingByGroupId(@Param(value = "groupId") Long groupId);

    @Query("SELECT event FROM Event event JOIN FETCH event.group containingGroup JOIN containingGroup.members membership WHERE membership.groupUserLinkId.userId = :userId AND event.endTimestamp > CURRENT_TIMESTAMP ORDER BY event.startTimestamp ASC")
    List<Event> findUpcomingWithGroupByUserId(@Param(value = "userId") Long userId);
}
