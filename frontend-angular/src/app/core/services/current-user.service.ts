import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

import { ApiUser } from "../api-models/ApiUser";

@Injectable({
    providedIn: "root"
})
export class CurrentUserService {
    private readonly API_PATH = "/api/current-user";

    constructor(private http: HttpClient) {}

    getCurrentUser() {
        return this.http.get<ApiUser>(this.API_PATH);
    }
}
