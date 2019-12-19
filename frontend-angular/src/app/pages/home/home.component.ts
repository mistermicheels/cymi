import { Component, OnInit } from "@angular/core";

import { CurrentUserService } from "../../core/services/current-user.service";

@Component({
    selector: "app-home",
    templateUrl: "./home.component.html",
    styleUrls: ["./home.component.css"]
})
export class HomeComponent implements OnInit {
    constructor(private currentUserService: CurrentUserService) {}

    ngOnInit() {
        // dummy call to trigger 401 if user is not logged in
        this.currentUserService.getCurrentUser().subscribe();
    }
}
