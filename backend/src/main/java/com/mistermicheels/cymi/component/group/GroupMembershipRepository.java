package com.mistermicheels.cymi.component.group;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mistermicheels.cymi.component.group.entity.GroupMembership;
import com.mistermicheels.cymi.component.group.entity.GroupUserLinkId;

interface GroupMembershipRepository extends JpaRepository<GroupMembership, GroupUserLinkId> {

    List<GroupMembership> findByGroupUserLinkIdGroupId(Long groupId);

    List<GroupMembership> findByGroupUserLinkIdUserIdAndGroupUserLinkIdGroupIdIn(Long userId,
            List<Long> groupIds);

    // @formatter:off
    @Query("SELECT membership "
            + "FROM GroupMembership membership "
            + "JOIN FETCH membership.group "
            + "WHERE membership.groupUserLinkId.userId = :userId")
    // @formatter:on
    List<GroupMembership> findWithGroupsByUserId(@Param(value = "userId") Long userId);

    // @formatter:off
    @Query("SELECT membership "
            + "FROM GroupMembership membership "
            + "JOIN FETCH membership.group "
            + "WHERE membership.groupUserLinkId.groupId = :groupId "
            + "AND membership.groupUserLinkId.userId = :userId")
    // @formatter:on
    Optional<GroupMembership> findWithGroupByGroupIdAndUserId(
            @Param(value = "groupId") Long groupId, @Param(value = "userId") Long userId);

}
