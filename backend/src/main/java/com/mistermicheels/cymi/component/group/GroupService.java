package com.mistermicheels.cymi.component.group;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mistermicheels.cymi.common.error.ForbiddenAccessException;
import com.mistermicheels.cymi.common.error.InvalidRequestException;
import com.mistermicheels.cymi.common.error.InvalidRequestExceptionType;
import com.mistermicheels.cymi.component.user.User;
import com.mistermicheels.cymi.component.user.UserService;

@Service
public class GroupService {

    private final UserService userService;
    private final GroupRepository repository;

    private final GroupInvitationRepository invitationRepository;
    private final GroupMembershipRepository membershipRepository;

    @Autowired
    public GroupService(UserService userService, GroupRepository repository,
            GroupInvitationRepository invitationRepository,
            GroupMembershipRepository membershipRepository) {
        this.userService = userService;
        this.repository = repository;
        this.invitationRepository = invitationRepository;
        this.membershipRepository = membershipRepository;
    }

    @Transactional
    public Group createGroup(String name, Long currentUserId, String currentUserDisplayName) {
        Group group = new Group(name);
        User currentUser = this.userService.getReference(currentUserId);

        GroupMembership membership = new GroupMembership(group, currentUser,
                GroupMembershipRole.Admin, currentUserDisplayName);

        this.repository.save(group);
        this.membershipRepository.save(membership);

        return group;
    }

    public void inviteUser(Long groupId, Long currentUserId, String userEmail,
            GroupMembershipRole role) {
        String notAnAdminMessage = "You are not an admin of this group";

        GroupMembership membership = this.membershipRepository
                .findWithGroupByGroupIdAndUserId(groupId, currentUserId)
                .orElseThrow(() -> new ForbiddenAccessException(notAnAdminMessage));

        if (membership.getRole() != GroupMembershipRole.Admin) {
            throw new ForbiddenAccessException(notAnAdminMessage);
        }

        String groupName = membership.getGroup().getName();
        User userToInvite = this.userService.findByEmailOrInviteNew(userEmail, groupName);

        this.checkUserCanBeInvitedToGroup(groupId, userToInvite.getId());

        Group groupReference = this.repository.getOne(groupId);

        GroupInvitation invitation = new GroupInvitation(groupReference, userToInvite, role);
        this.invitationRepository.save(invitation);
    }

    private void checkUserCanBeInvitedToGroup(Long groupId, Long userId) {
        // race condition same user concurrently invited to same group:
        // unlikely + caught by primary key

        // race condition user concurrently accepting invitation:
        // unlikely + impossible for both invitation and membership check (in that
        // order) to return false

        boolean alreadyInvited = this.invitationRepository
                .findById(new GroupUserLinkId(groupId, userId)).isPresent();

        if (alreadyInvited) {
            throw new InvalidRequestException("The user has already been invited to this group",
                    InvalidRequestExceptionType.UserAlreadyInvited);
        }

        boolean alreadyMember = this.membershipRepository
                .findById(new GroupUserLinkId(groupId, userId)).isPresent();

        if (alreadyMember) {
            throw new InvalidRequestException("The user is already a member of this group",
                    InvalidRequestExceptionType.UserAlreadyMember);
        }
    }

    @Transactional
    public void acceptGroupInvitation(Long groupId, Long currentUserId,
            String currentUserDisplayName) {
        GroupInvitation invitation = this.invitationRepository
                .findById(new GroupUserLinkId(groupId, currentUserId))
                .orElseThrow(() -> new InvalidRequestException(
                        "Unable to find invitation of this user to this group"));

        Group groupReference = this.repository.getOne(groupId);
        User userReference = this.userService.getReference(currentUserId);

        GroupMembership membership = new GroupMembership(groupReference, userReference,
                invitation.getRole(), currentUserDisplayName);

        this.membershipRepository.save(membership);
        this.invitationRepository.delete(invitation);
    }

    public GroupMembership findMembershipWithGroupByGroupAndUserIdOrThrow(Long id,
            Long currentUserId) {
        GroupMembership membership = this.membershipRepository
                .findWithGroupByGroupIdAndUserId(id, currentUserId).orElseThrow(
                        () -> new ForbiddenAccessException("You are not a member of this group"));

        return membership;
    }

    public GroupInvitation findInvitationWithGroupByGroupAndUserIdOrThrow(Long id,
            Long currentUserId) {
        GroupInvitation invitation = this.invitationRepository
                .findWithGroupByGroupIdAndUserId(id, currentUserId).orElseThrow(
                        () -> new ForbiddenAccessException("You are not invited to this group"));

        return invitation;
    }

    public List<GroupMembership> findMembershipsWithGroupsByUserId(Long userId) {
        return this.membershipRepository.findWithGroupsByUserId(userId);
    }

    public List<GroupInvitation> findInvitationsWithGroupsByUserId(Long userId) {
        return this.invitationRepository.findWithGroupsByUserId(userId);
    }

    public List<GroupMembership> findMembershipsByGroupId(Long groupId, Long currentUserId) {
        List<GroupMembership> memberships = this.membershipRepository
                .findByGroupUserLinkIdGroupId(groupId);

        boolean userIsMember = memberships.stream()
                .anyMatch(membership -> membership.getUserId() == currentUserId);

        if (!userIsMember) {
            throw new ForbiddenAccessException("You are not a member of this group");
        }

        return memberships;
    }

    public List<GroupInvitation> findInvitationsWithUserByGroupId(Long groupId,
            Long currentUserId) {
        this.checkCurrentUserAdmin(groupId, currentUserId);
        return this.invitationRepository.findWithUserByGroupUserLinkIdGroupId(groupId);
    }

    private void checkCurrentUserAdmin(Long groupId, Long currentUserId) {
        String notAnAdminMessage = "You are not an admin of this group";

        GroupMembership membership = this.membershipRepository
                .findById(new GroupUserLinkId(groupId, currentUserId))
                .orElseThrow(() -> new ForbiddenAccessException(notAnAdminMessage));

        if (membership.getRole() != GroupMembershipRole.Admin) {
            throw new ForbiddenAccessException(notAnAdminMessage);
        }
    }

}
