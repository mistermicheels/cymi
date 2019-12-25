import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { finalize } from "rxjs/operators";

import { AuthenticationService } from "../../core/services/authentication.service";

@Component({
    selector: "app-confirm-email",
    templateUrl: "./confirm-email.component.html",
    styleUrls: []
})
export class ConfirmEmailComponent implements OnInit {
    checkInProgress = true;
    checkSuccessful = false;

    constructor(
        private route: ActivatedRoute,
        private authenticationService: AuthenticationService
    ) {}

    ngOnInit() {
        const snapshot = this.route.snapshot;
        const token = snapshot.paramMap.get("token");

        if (token) {
            this.authenticationService
                .confirmEmail(token)
                .pipe(
                    finalize(() => {
                        this.checkInProgress = false;
                    })
                )
                .subscribe(
                    () => (this.checkSuccessful = true),
                    () => (this.checkSuccessful = false)
                );
        } else {
            this.checkInProgress = false;
            this.checkSuccessful = false;
        }
    }
}
