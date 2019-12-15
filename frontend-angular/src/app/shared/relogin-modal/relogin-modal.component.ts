import { Component } from "@angular/core";
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
        public activeModal: NgbActiveModal,
        private authenticationService: AuthenticationService
    ) {}

    login() {
        this.authenticationService.logIn(this.email, this.password).subscribe(
            () => {
                this.activeModal.close();
            },
            error => {
                this.loginErrorType = error.error.type;
            }
        );
    }
}
