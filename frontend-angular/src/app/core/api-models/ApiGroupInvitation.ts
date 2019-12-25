import { ApiGroupMembershipRole } from "./ApiGroupMembershipRole";

export interface ApiGroupInvitation {
    groupId: number;
    role: ApiGroupMembershipRole;
    email: string;
}
