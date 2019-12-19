import { Component } from "@angular/core";
import { Router } from "@angular/router";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";

import { AuthenticationService } from "../../core/services/authentication.service";

@Component({
    selector: "app-relogin-modal",
    templateUrl: "./relogin-modal.component.html",
    styleUrls: []
})
export class ReloginModalComponent {
    email = "";
    password = "";

    loginErrorType?: string;

    constructor(
        private activeModal: NgbActiveModal,
        private router: Router,
        private authenticationService: AuthenticationService
    ) {}

    relogin() {
        this.authenticationService.logIn(this.email, this.password).subscribe(
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
