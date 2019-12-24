import { Component, OnInit } from "@angular/core";

import { ApiGroupWithMembership } from "../../../core/api-models/ApiGroupWithMembership";
import { GroupsService } from "../../../core/services/groups.service";

@Component({
    selector: "app-groups",
    templateUrl: "./groups.component.html",
    styleUrls: []
})
export class GroupsComponent implements OnInit {
    joinedGroups?: ApiGroupWithMembership[];

    constructor(private groupsService: GroupsService) {}

    ngOnInit() {
        this.groupsService.getJoined().subscribe(
            joinedGroups =>
                (this.joinedGroups = joinedGroups.sort((a, b) => {
                    if (a.userRole === b.userRole) {
                        return a.name.localeCompare(b.name);
                    } else {
                        return a.userRole.localeCompare(b.userRole);
                    }
                }))
        );
    }
}
