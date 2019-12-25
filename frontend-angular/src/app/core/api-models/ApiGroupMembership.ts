import { ApiGroupMembershipRole } from "./ApiGroupMembershipRole";

export interface ApiGroupMembership {
    groupId: number;
    role: ApiGroupMembershipRole;
    displayName: string;
}
