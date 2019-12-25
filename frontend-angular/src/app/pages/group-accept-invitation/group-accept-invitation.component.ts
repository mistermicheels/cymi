import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";

import { ApiGroupWithInvitation } from "../../core/api-models/ApiGroupWithInvitation";
import { AuthenticationService } from "../../core/services/authentication.service";
import { GroupsService } from "../../core/services/groups.service";

@Component({
    selector: "app-group-accept-invitation",
    templateUrl: "./group-accept-invitation.component.html",
    styleUrls: []
})
export class GroupAcceptInvitationComponent implements OnInit {
    group?: ApiGroupWithInvitation;

    formGroup!: FormGroup;

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private authenticationService: AuthenticationService,
        private groupsService: GroupsService
    ) {}

    ngOnInit() {
        const snapshot = this.route.snapshot;
        const groupId = snapshot.paramMap.get("groupId");

        if (!groupId || isNaN(+groupId)) {
            this.router.navigateByUrl("/");
            return;
        }

        this.groupsService.getInvitedById(+groupId).subscribe(group => (this.group = group));

        const defaultDisplayName = this.authenticationService.getLoggedInUserDefaultDisplayName();

        this.formGroup = new FormGroup({
            displayName: new FormControl(defaultDisplayName, [Validators.required])
        });
    }

    acceptInvitation() {
        this.groupsService
            .acceptInvitation(this.group!.id, this.formGroup.get("displayName")!.value)
            .subscribe(() =>
                this.router.navigateByUrl(`/group/${this.group!.id}`, { replaceUrl: true })
            );
    }
}
