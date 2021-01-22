import { ApiEventResponseStatus } from "./ApiEventResponseStatus";

export interface ApiEvent {
    id: number;
    name: string;
    startTimestamp: string;
    endTimestamp: string;
    location: string;
    description?: string;
    ownStatus?: ApiEventResponseStatus;
    ownComment?: string;
    numberOtherYesResponses: number;
    numberOtherNoResponses: number;
    numberOtherMaybeResponses: number;
}
