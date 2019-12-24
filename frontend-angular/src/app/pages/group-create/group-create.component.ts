import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";

import { GroupsService } from "../../core/services/groups.service";

@Component({
    selector: "app-group-create",
    templateUrl: "./group-create.component.html",
    styleUrls: []
})
export class GroupCreateComponent implements OnInit {
    formGroup!: FormGroup;

    constructor(private router: Router, private groupsService: GroupsService) {}

    ngOnInit() {
        this.formGroup = new FormGroup({
            name: new FormControl("", [Validators.required]),
            userDisplayName: new FormControl("", [Validators.required])
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
