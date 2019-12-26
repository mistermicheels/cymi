import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";

import { ApiEventWithGroup } from "../../core/api-models/ApiEventWithGroup";
import { defaultDateFormat } from "../../core/defaults";
import { EventsService } from "../../core/services/events.service";

@Component({
    selector: "app-event",
    templateUrl: "./event.component.html",
    styleUrls: []
})
export class EventComponent implements OnInit {
    event?: ApiEventWithGroup;

    dateFormat = defaultDateFormat;

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private eventsService: EventsService
    ) {}

    ngOnInit() {
        const snapshot = this.route.snapshot;
        const eventId = snapshot.paramMap.get("eventId");

        if (!eventId || isNaN(+eventId)) {
            this.router.navigateByUrl("/");
            return;
        }

        this.eventsService.getByIdOrThrow(+eventId).subscribe(event => {
            this.event = event;
        });
    }
}
