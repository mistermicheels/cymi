import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { faCommentDots } from "@fortawesome/free-solid-svg-icons";

import { ApiEventResponse } from "../../core/api-models/ApiEventresponse";
import { ApiEventWithGroup } from "../../core/api-models/ApiEventWithGroup";
import { ApiGroupMembershipRole } from "../../core/api-models/ApiGroupMembershipRole";
import { defaultDateFormat } from "../../core/defaults";
import { EventsService } from "../../core/services/events.service";
import { compareApiEventResponseStatuses } from "../../core/util/compareApiEventResponseStatuses";

@Component({
    selector: "app-event",
    templateUrl: "./event.component.html",
    styleUrls: []
})
export class EventComponent implements OnInit {
    event?: ApiEventWithGroup;
    otherResponses?: ApiEventResponse[];

    expandedResponseCommentUserId?: number = undefined;

    responseFormGroup?: FormGroup;

    dateFormat = defaultDateFormat;

    faCommentDots = faCommentDots;

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

            if (event.userRoleInGroup === ApiGroupMembershipRole.Admin) {
                this.eventsService
                    .getOtherResponsesForEventOrThrow(eventId)
                    .subscribe(otherResponses => {
                        this.otherResponses = otherResponses.sort(this.responseComparator);
                    });
            }
        });
    }

    private responseComparator(a: ApiEventResponse, b: ApiEventResponse) {
        if (a.status !== b.status) {
            return compareApiEventResponseStatuses(a.status, b.status);
        } else {
            return a.userDisplayName.localeCompare(b.userDisplayName);
        }
    }

    toggleShowCommentForResponse(response: ApiEventResponse) {
        if (response.userId === this.expandedResponseCommentUserId) {
            this.expandedResponseCommentUserId = undefined;
        } else {
            this.expandedResponseCommentUserId = response.userId;
        }
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
