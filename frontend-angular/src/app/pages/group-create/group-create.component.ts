import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";

import { AuthenticationService } from "../../core/services/authentication.service";
import { GroupsService } from "../../core/services/groups.service";

@Component({
    selector: "app-group-create",
    templateUrl: "./group-create.component.html",
    styleUrls: []
})
export class GroupCreateComponent implements OnInit {
    formGroup!: FormGroup;

    constructor(
        private router: Router,
        private authenticationService: AuthenticationService,
        private groupsService: GroupsService
    ) {}

    ngOnInit() {
        const defaultDisplayName = this.authenticationService.getLoggedInUserDefaultDisplayName();

        this.formGroup = new FormGroup({
            name: new FormControl("", [Validators.required]),
            userDisplayName: new FormControl(defaultDisplayName, [Validators.required])
        });
    }

    createGroup() {
        this.groupsService
            .create(this.formGroup.get("name")!.value, this.formGroup.get("userDisplayName")!.value)
            .subscribe(response =>
                this.router.navigateByUrl(`/group/${response.id}`, { replaceUrl: true })
            );
    }
}
