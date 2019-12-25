import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";

import { ApiGroupInvitation } from "../../core/api-models/ApiGroupInvitation";
import { ApiGroupMembership } from "../../core/api-models/ApiGroupMembership";
import { ApiGroupMembershipRole } from "../../core/api-models/ApiGroupMembershipRole";
import { ApiGroupWithMembership } from "../../core/api-models/ApiGroupWithMembership";
import { GroupsService } from "../../core/services/groups.service";

@Component({
    selector: "app-group",
    templateUrl: "./group.component.html",
    styleUrls: []
})
export class GroupComponent implements OnInit {
    group?: ApiGroupWithMembership;
    members?: ApiGroupMembership[];
    invitees?: ApiGroupInvitation[];

    invitationFormGroup?: FormGroup;

    invitationErrorType?: string;

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private groupsService: GroupsService
    ) {}

    ngOnInit() {
        const snapshot = this.route.snapshot;
        const groupId = snapshot.paramMap.get("groupId");

        if (!groupId || isNaN(+groupId)) {
            this.router.navigateByUrl("/");
            return;
        }

        this.groupsService.getJoinedById(+groupId).subscribe(group => {
            this.group = group;
            this.retrieveMembers();

            if (this.group.userRole === ApiGroupMembershipRole.Admin) {
                this.retrieveInvitees();
                this.setUpInvitationForm();
            }
        });
    }

    private retrieveMembers() {
        this.groupsService.getMembersById(this.group!.id).subscribe(members => {
            this.members = members.sort(this.memberComparator);
        });
    }

    private memberComparator(a: ApiGroupMembership, b: ApiGroupMembership) {
        if (a.role === b.role) {
            return a.displayName.localeCompare(b.displayName);
        } else {
            return a.role.localeCompare(b.role);
        }
    }

    private retrieveInvitees() {
        this.groupsService.getInvitationsById(this.group!.id).subscribe(invitees => {
            this.invitees = invitees.sort(this.inviteeComparator);
        });
    }

    private inviteeComparator(a: ApiGroupInvitation, b: ApiGroupInvitation) {
        if (a.role === b.role) {
            return a.email.localeCompare(b.email);
        } else {
            return a.role.localeCompare(b.role);
        }
    }

    private setUpInvitationForm() {
        this.invitationFormGroup = new FormGroup({
            email: new FormControl("", [Validators.required, Validators.email]),
            makeAdmin: new FormControl(false, [Validators.required])
        });

        this.invitationFormGroup!.get("makeAdmin")!.valueChanges.subscribe(() => {
            this.resetMakeAdminValidation();
        });
    }

    resetMakeAdminValidation() {
        const makeAdminControl = this.invitationFormGroup!.get("makeAdmin")!;
        makeAdminControl.markAsPristine();
        makeAdminControl.markAsUntouched();
    }

    inviteUser() {
        let role: ApiGroupMembershipRole;

        if (this.invitationFormGroup!.get("makeAdmin")!.value) {
            role = ApiGroupMembershipRole.Admin;
        } else {
            role = ApiGroupMembershipRole.Member;
        }

        this.groupsService
            .inviteUser(this.group!.id, this.invitationFormGroup!.get("email")!.value, role)
            .subscribe(
                () => {
                    this.invitationErrorType = undefined;
                    this.retrieveInvitees();
                    this.resetInvitationForm();
                },
                error => {
                    this.invitationErrorType = error.error.type;
                }
            );
    }

    private resetInvitationForm() {
        this.invitationFormGroup!.get("email")!.setValue("");
        this.invitationFormGroup!.get("email")!.markAsPristine();
        this.invitationFormGroup!.get("makeAdmin")!.setValue(false);
    }
}
