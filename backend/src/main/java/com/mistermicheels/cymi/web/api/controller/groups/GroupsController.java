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

import com.mistermicheels.cymi.component.group.Group;
import com.mistermicheels.cymi.component.group.GroupInvitation;
import com.mistermicheels.cymi.component.group.GroupMembership;
import com.mistermicheels.cymi.component.group.GroupService;
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
        Group createdGroup = this.groupService.createGroup(input.name, userDetails.getId(),
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
        List<GroupMembership> memberships = this.groupService
                .findMembershipsWithGroupsByUserId(userDetails.getId());

        return memberships.stream().map(membership -> new ApiGroupWithMembership(membership))
                .collect(Collectors.toList());
    }

    @GetMapping("/invited")
    public List<ApiGroupWithInvitation> getInvitedGroups(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<GroupInvitation> invitations = this.groupService
                .findInvitationsWithGroupsByUserId(userDetails.getId());

        return invitations.stream().map(invitation -> new ApiGroupWithInvitation(invitation))
                .collect(Collectors.toList());
    }

    @GetMapping("/joined/id/{id}")
    public ApiGroupWithMembership getJoinedById(@PathVariable("id") Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        GroupMembership membership = this.groupService
                .findMembershipWithGroupByGroupAndUserIdOrThrow(id, userDetails.getId());

        return new ApiGroupWithMembership(membership);
    }

    @GetMapping("/invited/id/{id}")
    public ApiGroupWithInvitation getInvitedById(@PathVariable("id") Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        GroupInvitation invitation = this.groupService
                .findInvitationWithGroupByGroupAndUserIdOrThrow(id, userDetails.getId());

        return new ApiGroupWithInvitation(invitation);
    }

    @GetMapping("/id/{id}/members")
    public List<ApiGroupMembership> getMembersById(@PathVariable("id") Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<GroupMembership> memberships = this.groupService.findMembershipsByGroupId(id,
                userDetails.getId());

        return memberships.stream()
                .map(membership -> new ApiGroupMembership(membership, userDetails.getId()))
                .collect(Collectors.toList());
    }

    @GetMapping("/id/{id}/invitations")
    public List<ApiGroupInvitation> getInvitationsById(@PathVariable("id") Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<GroupInvitation> invitations = this.groupService.findInvitationsWithUserByGroupId(id,
                userDetails.getId());

        return invitations.stream().map(invitation -> new ApiGroupInvitation(invitation))
                .collect(Collectors.toList());
    }

}
