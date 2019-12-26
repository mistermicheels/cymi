import { Component, OnInit } from "@angular/core";

import { ApiEventWithGroup } from "../../core/api-models/ApiEventWithGroup";
import { EventsService } from "../../core/services/events.service";

@Component({
    selector: "app-home",
    templateUrl: "./home.component.html",
    styleUrls: []
})
export class HomeComponent implements OnInit {
    events?: ApiEventWithGroup[];

    constructor(private eventsService: EventsService) {}

    ngOnInit() {
        this.eventsService.getUpcomingForUser().subscribe(events => {
            this.events = events;
        });
    }
}
