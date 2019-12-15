import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";

import { AuthenticationService } from "../../core/services/authentication.service";

@Component({
    selector: "app-signup",
    templateUrl: "./signup.component.html",
    styleUrls: []
})
export class SignupComponent implements OnInit {
    email = "";
    password = "";
    defaultDisplayName = "";

    signupErrorType?: string;

    constructor(private router: Router, private authenticationService: AuthenticationService) {}

    ngOnInit() {}

    login() {
        this.authenticationService
            .signUp(this.email, this.password, this.defaultDisplayName)
            .subscribe(
                () => this.router.navigateByUrl("/"),
                error => {
                    this.signupErrorType = error.error.type;
                }
            );
    }
}
