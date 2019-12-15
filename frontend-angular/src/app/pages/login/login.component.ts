import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";

import { AuthenticationService } from "../../core/services/authentication.service";

@Component({
    selector: "app-login",
    templateUrl: "./login.component.html",
    styleUrls: []
})
export class LoginComponent implements OnInit {
    email = "";
    password = "";

    loginErrorType?: string;

    constructor(private router: Router, private authenticationService: AuthenticationService) {}

    ngOnInit() {}

    login() {
        this.authenticationService.logIn(this.email, this.password).subscribe(
            () => this.router.navigateByUrl("/"),
            error => {
                this.loginErrorType = error.error.type;
            }
        );
    }
}
