package com.mistermicheels.cymi.component.group;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface GroupInvitationRepository extends JpaRepository<GroupInvitation, GroupUserLinkId> {

    @Query("SELECT invitation FROM GroupInvitation invitation JOIN FETCH invitation.user WHERE invitation.groupUserLinkId.groupId = :groupId")
    List<GroupInvitation> findWithUserByGroupUserLinkIdGroupId(
            @Param(value = "groupId") Long groupId);

    @Query("SELECT invitation FROM GroupInvitation invitation JOIN FETCH invitation.group WHERE invitation.groupUserLinkId.userId = :userId")
    List<GroupInvitation> findWithGroupsByUserId(@Param(value = "userId") Long userId);

    @Query("SELECT invitation FROM GroupInvitation invitation JOIN FETCH invitation.group WHERE invitation.groupUserLinkId.groupId = :groupId AND invitation.groupUserLinkId.userId = :userId")
    Optional<GroupInvitation> findWithGroupByGroupIdAndUserId(
            @Param(value = "groupId") Long groupId, @Param(value = "userId") Long userId);

}
