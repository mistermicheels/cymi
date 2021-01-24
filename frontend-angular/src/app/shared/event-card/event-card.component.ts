import { Component, Input, OnInit } from "@angular/core";

import { ApiEvent } from "../../core/api-models/ApiEvent";
import { ApiGroupMembershipRole } from "../../core/api-models/ApiGroupMembershipRole";
import { defaultDateFormat } from "../../core/defaults";

@Component({
    selector: "app-event-card",
    templateUrl: "./event-card.component.html",
    styleUrls: []
})
export class EventCardComponent implements OnInit {
    @Input()
    event!: ApiEvent;

    @Input()
    groupData?: { groupName: string; userRoleInGroup: ApiGroupMembershipRole };

    dateFormat = defaultDateFormat;

    constructor() {}

    ngOnInit() {}
}
