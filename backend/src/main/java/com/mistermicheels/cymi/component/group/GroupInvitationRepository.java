package com.mistermicheels.cymi.component.group;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mistermicheels.cymi.component.group.entity.GroupInvitation;
import com.mistermicheels.cymi.component.group.entity.GroupUserLinkId;

interface GroupInvitationRepository extends JpaRepository<GroupInvitation, GroupUserLinkId> {

    // @formatter:off
    @Query("SELECT invitation "
            + "FROM GroupInvitation invitation "
            + "JOIN FETCH invitation.user "
            + "WHERE invitation.groupUserLinkId.groupId = :groupId")
    // @formatter:on
    List<GroupInvitation> findWithUserByGroupUserLinkIdGroupId(
            @Param(value = "groupId") Long groupId);

    // @formatter:off
    @Query("SELECT invitation "
            + "FROM GroupInvitation invitation "
            + "JOIN FETCH invitation.group "
            + "WHERE invitation.groupUserLinkId.userId = :userId")
    // @formatter:on
    List<GroupInvitation> findWithGroupsByUserId(@Param(value = "userId") Long userId);

    // @formatter:off
    @Query("SELECT invitation "
            + "FROM GroupInvitation invitation "
            + "JOIN FETCH invitation.group "
            + "WHERE invitation.groupUserLinkId.groupId = :groupId "
            + "AND invitation.groupUserLinkId.userId = :userId")
    // @formatter:on
    Optional<GroupInvitation> findWithGroupByGroupIdAndUserId(
            @Param(value = "groupId") Long groupId, @Param(value = "userId") Long userId);

}
