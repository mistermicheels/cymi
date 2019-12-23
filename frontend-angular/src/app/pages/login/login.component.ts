import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";

import { AuthenticationService } from "../../core/services/authentication.service";

@Component({
    selector: "app-login",
    templateUrl: "./login.component.html",
    styleUrls: []
})
export class LoginComponent implements OnInit {
    formGroup!: FormGroup;

    loginErrorType?: string;

    constructor(private router: Router, private authenticationService: AuthenticationService) {}

    ngOnInit() {
        this.formGroup = new FormGroup({
            email: new FormControl("", [Validators.required, Validators.email]),
            password: new FormControl("", [Validators.required])
        });
    }

    login() {
        this.authenticationService
            .logIn(this.formGroup.get("email")!.value, this.formGroup.get("password")!.value)
            .subscribe(
                () => this.router.navigateByUrl("/"),
                error => {
                    this.loginErrorType = error.error.type;
                }
            );
    }
}
