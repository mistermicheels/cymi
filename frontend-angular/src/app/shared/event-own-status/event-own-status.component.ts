import { Component, Input } from "@angular/core";
import { faCheck, faQuestion, faTimes } from "@fortawesome/free-solid-svg-icons";

import { ApiEvent } from "../../core/api-models/ApiEvent";

@Component({
    selector: "app-event-own-status",
    templateUrl: "./event-own-status.component.html",
    styleUrls: []
})
export class EventOwnStatusComponent {
    @Input()
    event!: ApiEvent;

    faCheck = faCheck;
    faTimes = faTimes;
    faQuestion = faQuestion;

    constructor() {}
}
