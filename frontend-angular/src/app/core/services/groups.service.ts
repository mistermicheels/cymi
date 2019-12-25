import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

import { ApiGroupInvitation } from "../api-models/ApiGroupInvitation";
import { ApiGroupMembership } from "../api-models/ApiGroupMembership";
import { ApiGroupMembershipRole } from "../api-models/ApiGroupMembershipRole";
import { ApiGroupWithInvitation } from "../api-models/ApiGroupWithInvitation";
import { ApiGroupWithMembership } from "../api-models/ApiGroupWithMembership";
import { ApiSuccessResponseWithId } from "../api-models/ApiSuccessResponseWithId";

@Injectable({
    providedIn: "root"
})
export class GroupsService {
    private readonly API_PATH = "/api/groups";

    constructor(private http: HttpClient) {}

    create(name: string, userDisplayName: string) {
        return this.http.post<ApiSuccessResponseWithId>(this.API_PATH, { name, userDisplayName });
    }

    inviteUser(groupId: number, email: string, role: ApiGroupMembershipRole) {
        return this.http.post<void>(this.API_PATH + `/id/${groupId}/invite-user`, { email, role });
    }

    acceptInvitation(groupId: number, displayName: string) {
        return this.http.post<void>(this.API_PATH + `/id/${groupId}/accept-invitation`, {
            displayName
        });
    }

    getJoined() {
        return this.http.get<ApiGroupWithMembership[]>(this.API_PATH + "/joined");
    }

    getInvited() {
        return this.http.get<ApiGroupWithInvitation[]>(this.API_PATH + "/invited");
    }
    getJoinedById(id: number) {
        return this.http.get<ApiGroupWithMembership>(this.API_PATH + `/joined/id/${id}`);
    }

    getInvitedById(id: number) {
        return this.http.get<ApiGroupWithInvitation>(this.API_PATH + `/invited/id/${id}`);
    }

    getMembersById(id: number) {
        return this.http.get<ApiGroupMembership[]>(this.API_PATH + `/id/${id}/members`);
    }

    getInvitationsById(id: number) {
        return this.http.get<ApiGroupInvitation[]>(this.API_PATH + `/id/${id}/invitations`);
    }
}
