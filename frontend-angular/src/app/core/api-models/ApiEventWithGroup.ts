import { ApiEvent } from "./ApiEvent";

export interface ApiEventWithGroup extends ApiEvent {
    groupId: number;
    groupName: string;
}
