import { Component, Input, OnInit } from "@angular/core";

import { ApiEvent } from "../../core/api-models/ApiEvent";
import { defaultDateFormat } from "../../core/defaults";

@Component({
    selector: "app-event-card",
    templateUrl: "./event-card.component.html",
    styleUrls: []
})
export class EventCardComponent implements OnInit {
    @Input()
    event!: ApiEvent;

    @Input()
    groupName?: string;

    dateFormat = defaultDateFormat;

    constructor() {}

    ngOnInit() {}
}
