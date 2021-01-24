import { ApiEventResponseStatus } from "./ApiEventResponseStatus";

export interface ApiEventResponse {
    status: ApiEventResponseStatus;
    comment?: string;
    userId: number;
    userDisplayName: string;
}
