import { ApiGroupMembershipRole } from "./ApiGroupMembershipRole";

export interface ApiGroupWithInvitation {
    id: number;
    name: string;
    userRole: ApiGroupMembershipRole;
}
