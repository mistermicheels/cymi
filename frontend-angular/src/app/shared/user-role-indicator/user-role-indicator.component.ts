import { Component, Input } from "@angular/core";

import { ApiGroupMembershipRole } from "../../core/api-models/ApiGroupMembershipRole";

@Component({
    selector: "app-user-role-indicator",
    templateUrl: "./user-role-indicator.component.html",
    styleUrls: []
})
export class UserRoleIndicatorComponent {
    @Input()
    role!: ApiGroupMembershipRole;

    constructor() {}
}
