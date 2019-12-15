import { Component, OnInit } from "@angular/core";

import { CurrentUserService } from "../../core/services/current-user.service";

@Component({
    selector: "app-my-account",
    templateUrl: "./my-account.component.html",
    styleUrls: []
})
export class MyAccountComponent implements OnInit {
    email = "";
    defaultDisplayName = "";

    constructor(private currentUserService: CurrentUserService) {}

    ngOnInit() {
        this.currentUserService.getCurrentUser().subscribe(user => {
            this.email = user.email;
            this.defaultDisplayName = user.defaultDisplayName;
        });
    }
}
