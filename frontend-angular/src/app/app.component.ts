import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";

import { AuthenticationService } from "./core/services/authentication.service";

@Component({
    selector: "app-root",
    templateUrl: "./app.component.html",
    styleUrls: ["./app.component.css"]
})
export class AppComponent implements OnInit {
    isMenuCollapsed = true;
    isUserLoggedIn = false;

    constructor(private router: Router, private authenticationService: AuthenticationService) {}

    ngOnInit() {
        this.authenticationService.isUserLoggedIn$.subscribe(
            isUserLoggedIn => (this.isUserLoggedIn = isUserLoggedIn)
        );

        this.authenticationService.initializeLoggedInUser();
    }

    toggleMenuCollapsed() {
        this.isMenuCollapsed = !this.isMenuCollapsed;
    }

    collapseMenu() {
        this.isMenuCollapsed = true;
    }

    logOut() {
        this.collapseMenu();

        this.authenticationService.logOut().subscribe(() => this.router.navigateByUrl("/login"));
    }
}
