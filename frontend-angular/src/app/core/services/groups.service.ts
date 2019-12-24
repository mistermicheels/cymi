import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

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

    getJoined() {
        return this.http.get<ApiGroupWithMembership[]>(this.API_PATH + "/joined");
    }

    getJoinedById(id: number) {
        return this.http.get<ApiGroupWithMembership>(this.API_PATH + `/joined/id/${id}`);
    }
}
