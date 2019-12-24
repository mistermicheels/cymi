package com.mistermicheels.cymi.component.group;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
class GroupUserLinkId implements Serializable {

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "user_id")
    private Long userId;

    public GroupUserLinkId() {
    }

    public GroupUserLinkId(Long groupId, Long userId) {
        this.groupId = groupId;
        this.userId = userId;
    }

    Long getGroupId() {
        return this.groupId;
    }

    Long getUserId() {
        return this.userId;
    }

    // auto-generated equals and hashCode

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        GroupUserLinkId other = (GroupUserLinkId) obj;
        if (this.groupId == null) {
            if (other.groupId != null)
                return false;
        } else if (!this.groupId.equals(other.groupId))
            return false;
        if (this.userId == null) {
            if (other.userId != null)
                return false;
        } else if (!this.userId.equals(other.userId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.groupId == null) ? 0 : this.groupId.hashCode());
        result = prime * result + ((this.userId == null) ? 0 : this.userId.hashCode());
        return result;
    }

}
