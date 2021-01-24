import { Component, Input } from "@angular/core";
import { faCheck, faQuestion, faTimes } from "@fortawesome/free-solid-svg-icons";

import { ApiEventResponseStatus } from "../../core/api-models/ApiEventResponseStatus";

@Component({
    selector: "app-event-response-status",
    templateUrl: "./event-response-status.component.html",
    styleUrls: []
})
export class EventResponseStatusComponent {
    @Input()
    status!: ApiEventResponseStatus;

    @Input()
    includeText?: boolean;

    faCheck = faCheck;
    faTimes = faTimes;
    faQuestion = faQuestion;

    constructor() {}
}
