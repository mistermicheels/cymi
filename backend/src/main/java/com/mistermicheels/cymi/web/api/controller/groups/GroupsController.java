package com.mistermicheels.cymi.web.api.controller.groups;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mistermicheels.cymi.component.group.GroupService;
import com.mistermicheels.cymi.component.group.entity.IGroup;
import com.mistermicheels.cymi.component.group.entity.IGroupInvitationWithGroup;
import com.mistermicheels.cymi.component.group.entity.IGroupInvitationWithUser;
import com.mistermicheels.cymi.component.group.entity.IGroupMembership;
import com.mistermicheels.cymi.component.group.entity.IGroupMembershipWithGroup;
import com.mistermicheels.cymi.config.security.CustomUserDetails;
import com.mistermicheels.cymi.web.api.controller.groups.input.AcceptInvitationInput;
import com.mistermicheels.cymi.web.api.controller.groups.input.CreateGroupInput;
import com.mistermicheels.cymi.web.api.controller.groups.input.InviteUserInput;
import com.mistermicheels.cymi.web.api.output.ApiGroupInvitation;
import com.mistermicheels.cymi.web.api.output.ApiGroupMembership;
import com.mistermicheels.cymi.web.api.output.ApiGroupWithInvitation;
import com.mistermicheels.cymi.web.api.output.ApiGroupWithMembership;
import com.mistermicheels.cymi.web.api.output.ApiSuccessResponse;

@RestController()
@RequestMapping("groups")
public class GroupsController {

    private final GroupService groupService;

    @Autowired
    public GroupsController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping()
    public ApiSuccessResponse create(@Valid @RequestBody CreateGroupInput input,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        IGroup createdGroup = this.groupService.createGroup(input.name, userDetails.getId(),
                input.userDisplayName);

        return new ApiSuccessResponse(createdGroup.getId());
    }

    @PostMapping("id/{id}/invite-user")
    public ApiSuccessResponse inviteUser(@PathVariable("id") Long id,
            @Valid @RequestBody InviteUserInput input,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        this.groupService.inviteUser(id, userDetails.getId(), input.email, input.role);
        return new ApiSuccessResponse();
    }

    @PostMapping("/id/{id}/accept-invitation")
    public ApiSuccessResponse acceptInvitation(@PathVariable("id") Long id,
            @Valid @RequestBody AcceptInvitationInput input,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        this.groupService.acceptGroupInvitation(id, userDetails.getId(), input.displayName);
        return new ApiSuccessResponse();
    }

    @GetMapping("/joined")
    public List<ApiGroupWithMembership> getJoined(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<? extends IGroupMembershipWithGroup> memberships = this.groupService
                .findMembershipsWithGroupsForUser(userDetails.getId());

        return memberships.stream().map(membership -> new ApiGroupWithMembership(membership))
                .collect(Collectors.toList());
    }

    @GetMapping("/invited")
    public List<ApiGroupWithInvitation> getInvitedGroups(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<? extends IGroupInvitationWithGroup> invitations = this.groupService
                .findInvitationsWithGroupsForUser(userDetails.getId());

        return invitations.stream().map(invitation -> new ApiGroupWithInvitation(invitation))
                .collect(Collectors.toList());
    }

    @GetMapping("/joined/id/{id}")
    public ApiGroupWithMembership getJoinedById(@PathVariable("id") Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        IGroupMembershipWithGroup membership = this.groupService
                .findMembershipWithGroupForGroupAndUserOrThrow(id, userDetails.getId());

        return new ApiGroupWithMembership(membership);
    }

    @GetMapping("/invited/id/{id}")
    public ApiGroupWithInvitation getInvitedById(@PathVariable("id") Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        IGroupInvitationWithGroup invitation = this.groupService
                .findInvitationWithGroupForGroupAndUserOrThrow(id, userDetails.getId());

        return new ApiGroupWithInvitation(invitation);
    }

    @GetMapping("/id/{id}/members")
    public List<ApiGroupMembership> getMembersById(@PathVariable("id") Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<? extends IGroupMembership> memberships = this.groupService.findMembershipsForGroup(id,
                userDetails.getId());

        return memberships.stream()
                .map(membership -> new ApiGroupMembership(membership, userDetails.getId()))
                .collect(Collectors.toList());
    }

    @GetMapping("/id/{id}/invitations")
    public List<ApiGroupInvitation> getInvitationsById(@PathVariable("id") Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<? extends IGroupInvitationWithUser> invitations = this.groupService
                .findInvitationsWithUserForGroup(id, userDetails.getId());

        return invitations.stream().map(invitation -> new ApiGroupInvitation(invitation))
                .collect(Collectors.toList());
    }

}
