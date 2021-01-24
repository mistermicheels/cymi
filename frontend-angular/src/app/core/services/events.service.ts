import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

import { ApiEventResponse } from "../api-models/ApiEventresponse";
import { ApiEventResponseStatus } from "../api-models/ApiEventResponseStatus";
import { ApiEventWithGroup } from "../api-models/ApiEventWithGroup";
import { ApiSuccessResponseWithId } from "../api-models/ApiSuccessResponseWithId";

@Injectable({
    providedIn: "root"
})
export class EventsService {
    private readonly API_PATH = "/api/events";

    constructor(private http: HttpClient) {}

    create(
        groupId: number,
        name: string,
        startTimestamp: string,
        endTimestamp: string,
        location: string,
        description?: string
    ) {
        return this.http.post<ApiSuccessResponseWithId>(this.API_PATH, {
            groupId,
            name,
            startTimestamp,
            endTimestamp,
            location,
            description
        });
    }

    respond(eventId: number, status: ApiEventResponseStatus, comment?: string) {
        return this.http.post<ApiSuccessResponseWithId>(this.API_PATH + "/respond", {
            eventId,
            status,
            comment
        });
    }

    getByIdOrThrow(id: number) {
        return this.http.get<ApiEventWithGroup>(this.API_PATH + `/id/${id}`);
    }

    getOtherResponsesForEventOrThrow(id: number) {
        return this.http.get<ApiEventResponse[]>(this.API_PATH + `/id/${id}/other_responses`);
    }

    getUpcomingForUser() {
        return this.http.get<ApiEventWithGroup[]>(this.API_PATH + "/upcoming/user");
    }

    getUpcomingByGroup(groupId: number) {
        return this.http.get<ApiEventWithGroup[]>(this.API_PATH + `/upcoming/group/${groupId}`);
    }
}
