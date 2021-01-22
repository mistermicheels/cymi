import { Component, Input } from "@angular/core";
import { faCheck, faQuestion, faTimes } from "@fortawesome/free-solid-svg-icons";

import { ApiEvent } from "../../core/api-models/ApiEvent";

@Component({
    selector: "app-event-other-statuses",
    templateUrl: "./event-other-statuses.component.html",
    styleUrls: []
})
export class EventOtherStatusesComponent {
    @Input()
    event!: ApiEvent;

    faCheck = faCheck;
    faTimes = faTimes;
    faQuestion = faQuestion;

    constructor() {}
}
