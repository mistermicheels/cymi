import { ApiEvent } from "./ApiEvent";
import { ApiGroupMembershipRole } from "./ApiGroupMembershipRole";

export interface ApiEventWithGroup extends ApiEvent {
    groupId: number;
    groupName: string;
    userRoleInGroup: ApiGroupMembershipRole;
}
