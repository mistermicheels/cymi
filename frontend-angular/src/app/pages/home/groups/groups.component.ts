import { Component, OnInit } from "@angular/core";

import { ApiGroupWithInvitation } from "../../../core/api-models/ApiGroupWithInvitation";
import { ApiGroupWithMembership } from "../../../core/api-models/ApiGroupWithMembership";
import { GroupsService } from "../../../core/services/groups.service";

@Component({
    selector: "app-groups",
    templateUrl: "./groups.component.html",
    styleUrls: []
})
export class GroupsComponent implements OnInit {
    joinedGroups?: ApiGroupWithMembership[];
    invitedGroups?: ApiGroupWithInvitation[];

    constructor(private groupsService: GroupsService) {}

    ngOnInit() {
        this.groupsService
            .getJoined()
            .subscribe(
                joinedGroups => (this.joinedGroups = joinedGroups.sort(this.groupComparator))
            );

        this.groupsService
            .getInvited()
            .subscribe(
                invitedGroups => (this.invitedGroups = invitedGroups.sort(this.groupComparator))
            );
    }

    private groupComparator(
        a: ApiGroupWithMembership | ApiGroupWithInvitation,
        b: ApiGroupWithMembership | ApiGroupWithInvitation
    ) {
        if (a.userRole !== b.userRole) {
            return a.userRole.localeCompare(b.userRole);
        } else {
            return a.name.localeCompare(b.name);
        }
    }
}
