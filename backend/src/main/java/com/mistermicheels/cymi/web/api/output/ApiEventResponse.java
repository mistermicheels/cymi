package com.mistermicheels.cymi.web.api.output;

import com.mistermicheels.cymi.component.event.EventResponseStatus;
import com.mistermicheels.cymi.component.event.entity.EventResponse;
import com.mistermicheels.cymi.component.group.GroupMembershipRole;
import com.mistermicheels.cymi.component.group.entity.GroupMembership;

public class ApiEventResponse {

    private final EventResponseStatus status;
    private final String comment;
    private final Long userId;
    private final String userDisplayName;
    private final GroupMembershipRole userRoleInGroup;

    public ApiEventResponse(EventResponse eventResponse,
            GroupMembership correspondingUserMembership) {
        this.status = eventResponse.getStatus();
        this.comment = eventResponse.getComment().orElse(null);
        this.userId = correspondingUserMembership.getUserId();
        this.userDisplayName = correspondingUserMembership.getDisplayName();
        this.userRoleInGroup = correspondingUserMembership.getRole();
    }

    public EventResponseStatus getStatus() {
        return this.status;
    }

    public String getComment() {
        return this.comment;
    }

    public Long getUserId() {
        return this.userId;
    }

    public String getUserDisplayName() {
        return this.userDisplayName;
    }

    public GroupMembershipRole getUserRoleInGroup() {
        return this.userRoleInGroup;
    }

}
