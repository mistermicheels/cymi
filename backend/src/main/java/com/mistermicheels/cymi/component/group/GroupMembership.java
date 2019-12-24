package com.mistermicheels.cymi.component.group;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.mistermicheels.cymi.common.FieldLengths;
import com.mistermicheels.cymi.component.user.User;

@Entity
@Table(name = "group_membership")
public class GroupMembership implements GroupUserLink {

    @EmbeddedId
    private GroupUserLinkId groupUserLinkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    @MapsId("groupId")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @MapsId("userId")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = FieldLengths.DEFAULT_STRING_LENGTH)
    private GroupMembershipRole role;

    @Column(nullable = false, length = FieldLengths.DEFAULT_STRING_LENGTH)
    private String displayName;

    GroupMembership() {
    }

    GroupMembership(Group group, User user, GroupMembershipRole role, String displayName) {
        this.groupUserLinkId = new GroupUserLinkId(group.getId(), user.getId());
        this.group = group;
        this.user = user;
        this.role = role;
        this.displayName = displayName;
    }

    @Override
    public Group getGroup() {
        return this.group;
    }

    @Override
    public Long getGroupId() {
        return this.groupUserLinkId.getGroupId();
    }

    @Override
    public Long getUserId() {
        return this.groupUserLinkId.getUserId();
    }

    @Override
    public GroupMembershipRole getRole() {
        return this.role;
    }

    public String getDisplayName() {
        return this.displayName;
    }

}
