package com.mistermicheels.cymi.web.api.controller.groups.input;

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
import com.mistermicheels.cymi.component.group.GroupMembership;
import com.mistermicheels.cymi.component.group.GroupService;
import com.mistermicheels.cymi.config.security.CustomUserDetails;
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

    @GetMapping("/joined")
    public List<ApiGroupWithMembership> getJoined(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<GroupMembership> memberships = this.groupService
                .findMembershipsWithGroupsByUserId(userDetails.getId());

        return memberships.stream().map(membership -> new ApiGroupWithMembership(membership))
                .collect(Collectors.toList());
    }

    @GetMapping("/joined/id/{id}")
    public ApiGroupWithMembership getJoinedById(@PathVariable("id") Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        GroupMembership membership = this.groupService
                .findMembershipWithGroupByGroupAndUserIdOrThrow(id, userDetails.getId());

        return new ApiGroupWithMembership(membership);
    }

}
