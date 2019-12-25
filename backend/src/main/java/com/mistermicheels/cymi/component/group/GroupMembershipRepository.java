package com.mistermicheels.cymi.component.group;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface GroupMembershipRepository extends JpaRepository<GroupMembership, GroupUserLinkId> {

    List<GroupMembership> findByGroupUserLinkIdGroupId(Long groupId);

    @Query("SELECT membership FROM GroupMembership membership JOIN FETCH membership.group WHERE membership.groupUserLinkId.userId = :userId")
    List<GroupMembership> findWithGroupsByUserId(@Param(value = "userId") Long userId);

    @Query("SELECT membership FROM GroupMembership membership JOIN FETCH membership.group WHERE membership.groupUserLinkId.groupId = :groupId AND membership.groupUserLinkId.userId = :userId")
    Optional<GroupMembership> findWithGroupByGroupIdAndUserId(
            @Param(value = "groupId") Long groupId, @Param(value = "userId") Long userId);

}
