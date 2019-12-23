import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";

import { AuthenticationService } from "../../core/services/authentication.service";

@Component({
    selector: "app-signup",
    templateUrl: "./signup.component.html",
    styleUrls: []
})
export class SignupComponent implements OnInit {
    formGroup!: FormGroup;

    signupErrorType?: string;

    constructor(private router: Router, private authenticationService: AuthenticationService) {}

    ngOnInit() {
        this.formGroup = new FormGroup({
            email: new FormControl("", [Validators.required, Validators.email]),
            password: new FormControl("", [Validators.required]),
            defaultDisplayName: new FormControl("", [Validators.required])
        });
    }

    signUp() {
        this.authenticationService
            .signUp(
                this.formGroup.get("email")!.value,
                this.formGroup.get("password")!.value,
                this.formGroup.get("defaultDisplayName")!.value
            )
            .subscribe(
                () => this.router.navigateByUrl("/login"),
                error => {
                    this.signupErrorType = error.error.type;
                }
            );
    }
}
