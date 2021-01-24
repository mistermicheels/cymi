import { ApiEventResponseStatus } from "../api-models/ApiEventResponseStatus";

const statusesInOrder = [
    ApiEventResponseStatus.Yes,
    ApiEventResponseStatus.No,
    ApiEventResponseStatus.Maybe
];

export function compareApiEventResponseStatuses(
    a: ApiEventResponseStatus,
    b: ApiEventResponseStatus
) {
    return statusesInOrder.indexOf(a) - statusesInOrder.indexOf(b);
}
