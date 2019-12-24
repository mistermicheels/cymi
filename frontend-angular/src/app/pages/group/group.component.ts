import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";

import { ApiGroupWithMembership } from "../../core/api-models/ApiGroupWithMembership";
import { GroupsService } from "../../core/services/groups.service";

@Component({
    selector: "app-group",
    templateUrl: "./group.component.html",
    styleUrls: []
})
export class GroupComponent implements OnInit {
    group?: ApiGroupWithMembership;

    constructor(private route: ActivatedRoute, private groupsService: GroupsService) {}

    ngOnInit() {
        const snapshot = this.route.snapshot;
        const groupId = snapshot.paramMap.get("groupId");

        if (groupId && !isNaN(+groupId)) {
            this.groupsService.getJoinedById(+groupId).subscribe(group => (this.group = group));
        }
    }
}
