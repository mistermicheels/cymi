import { Injectable } from "@angular/core";
import { CanActivate, Router } from "@angular/router";
import { Observable } from "rxjs";
import { take, tap } from "rxjs/operators";

import { AuthenticationService } from "./services/authentication.service";

@Injectable({
    providedIn: "root"
})
export class AuthGuard implements CanActivate {
    constructor(private router: Router, private authenticationService: AuthenticationService) {}

    canActivate(): Observable<boolean> {
        return this.authenticationService.isUserLoggedIn$.pipe(
            take(1),
            tap(loggedIn => {
                if (!loggedIn) {
                    this.router.navigateByUrl("/login");
                }
            })
        );
    }
}
