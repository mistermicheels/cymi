import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";

import { AuthenticationService } from "../../core/services/authentication.service";

@Component({
    selector: "app-relogin-modal",
    templateUrl: "./relogin-modal.component.html",
    styleUrls: []
})
export class ReloginModalComponent implements OnInit {
    formGroup!: FormGroup;

    email = "";

    loginErrorType?: string;

    constructor(
        private activeModal: NgbActiveModal,
        private router: Router,
        private authenticationService: AuthenticationService
    ) {}

    ngOnInit() {
        this.formGroup = new FormGroup({
            password: new FormControl("", [Validators.required])
        });
    }

    relogin() {
        this.authenticationService
            .logIn(this.email, this.formGroup.get("password")!.value)
            .subscribe(
                () => {
                    this.activeModal.close();
                },
                error => {
                    this.loginErrorType = error.error.type;
                }
            );
    }

    goToLogin() {
        this.router.navigateByUrl("/login");
        this.activeModal.dismiss();
    }

    goToSignup() {
        this.router.navigateByUrl("/signup");
        this.activeModal.dismiss();
    }
}
