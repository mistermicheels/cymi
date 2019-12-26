import { Component, OnInit } from "@angular/core";
import {
    AbstractControl,
    FormControl,
    FormGroup,
    ValidationErrors,
    ValidatorFn,
    Validators
} from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { ErrorMessage } from "ng-bootstrap-form-validation";

import { ApiGroup } from "../../core/api-models/ApiGroup";
import { EventsService } from "../../core/services/events.service";
import { GroupsService } from "../../core/services/groups.service";

@Component({
    selector: "app-event-create",
    templateUrl: "./event-create.component.html",
    styleUrls: []
})
export class EventCreateComponent implements OnInit {
    group?: ApiGroup;

    formGroup!: FormGroup;

    now = new Date().toISOString();
    test = "test";

    customErrorMessages: ErrorMessage[] = [
        {
            error: "endNotAfterStart",
            format: () => `End of event must be after start of event`
        }
    ];

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private groupsService: GroupsService,
        private eventsService: EventsService
    ) {}

    ngOnInit() {
        const snapshot = this.route.snapshot;
        const groupId = snapshot.paramMap.get("groupId");

        if (groupId && !isNaN(+groupId)) {
            this.retrieveGroup(+groupId);
        } else {
            this.router.navigateByUrl("/");
            return;
        }

        this.formGroup = new FormGroup({
            name: new FormControl("", [Validators.required]),
            startTimestamp: new FormControl("", [Validators.required]),
            endTimestamp: new FormControl("", [
                Validators.required,
                this.endAfterStartValidator("startTimestamp", "endTimestamp")
            ]),
            location: new FormControl("", [Validators.required]),
            description: new FormControl(undefined, [])
        });
    }

    private retrieveGroup(groupId: number) {
        this.groupsService.getJoinedById(groupId).subscribe(
            group => (this.group = group),
            () => this.router.navigateByUrl("/")
        );
    }

    private endAfterStartValidator(startKey: string, endKey: string): ValidatorFn {
        return (control: AbstractControl): ValidationErrors | null => {
            const parent = control.parent;

            if (!parent) {
                return null;
            }

            const start = control.parent.get(startKey)!.value;
            const end = control.parent.get(endKey)!.value;

            if (start && end && end <= start) {
                return { endNotAfterStart: true };
            }

            return null;
        };
    }

    getStartTimestamp() {
        return this.formGroup.get("startTimestamp")!.value;
    }

    createEvent() {
        this.eventsService
            .create(
                this.group!.id,
                this.formGroup.get("name")!.value,
                this.formGroup.get("startTimestamp")!.value,
                this.formGroup.get("endTimestamp")!.value,
                this.formGroup.get("location")!.value,
                this.formGroup.get("description")!.value || undefined
            )
            .subscribe(response =>
                this.router.navigateByUrl(`/event/${response.id}`, { replaceUrl: true })
            );
    }
}
