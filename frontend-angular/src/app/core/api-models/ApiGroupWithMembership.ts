import { ApiGroupMembershipRole } from "./ApiGroupMembershipRole";

export interface ApiGroupWithMembership {
    id: number;
    name: string;
    userRole: ApiGroupMembershipRole;
    userDisplayName: string;
}
