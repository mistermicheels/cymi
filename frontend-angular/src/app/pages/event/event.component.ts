import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { faCheck, faQuestion, faTimes } from "@fortawesome/free-solid-svg-icons";

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

    responseFormGroup?: FormGroup;

    dateFormat = defaultDateFormat;

    faCheck = faCheck;
    faTimes = faTimes;
    faQuestion = faQuestion;

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

        this.retrieveEvent(+eventId);
    }

    private retrieveEvent(eventId: number) {
        this.eventsService.getByIdOrThrow(eventId).subscribe(event => {
            this.event = event;

            if (!event.ownStatus) {
                this.responseFormGroup = new FormGroup({
                    status: new FormControl("", [Validators.required]),
                    comment: new FormControl(undefined, [])
                });
            }
        });
    }

    respond() {
        this.eventsService
            .respond(
                this.event!.id,
                this.responseFormGroup!.get("status")!.value,
                this.responseFormGroup!.get("comment")!.value
            )
            .subscribe(() => {
                this.retrieveEvent(this.event!.id);
            });
    }
}
