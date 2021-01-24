import { Component, Input } from "@angular/core";

import { ApiEvent } from "../../core/api-models/ApiEvent";

@Component({
    selector: "app-event-other-statuses",
    templateUrl: "./event-other-statuses.component.html",
    styleUrls: []
})
export class EventOtherStatusesComponent {
    @Input()
    event!: ApiEvent;

    constructor() {}
}
