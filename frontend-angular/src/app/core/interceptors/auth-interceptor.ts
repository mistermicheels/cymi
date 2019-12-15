import {
    HttpErrorResponse,
    HttpEvent,
    HttpHandler,
    HttpInterceptor,
    HttpRequest
} from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { EMPTY, Observable } from "rxjs";
import { catchError, switchMap, take } from "rxjs/operators";

import { AuthenticationService } from "../services/authentication.service";
import { ReloginService } from "../services/relogin.service";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
    constructor(
        private router: Router,
        private authenticationService: AuthenticationService,
        private reloginService: ReloginService
    ) {}

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request).pipe(
            catchError((error: HttpErrorResponse) => {
                if (error.status === 401) {
                    return this.handle401(error, request, next);
                } else if (error.status === 403) {
                    return this.handle403(error);
                } else {
                    throw error;
                }
            })
        );
    }

    private handle401(error: HttpErrorResponse, request: HttpRequest<any>, next: HttpHandler) {
        if (this.authenticationService.isUserLoggedIn()) {
            const email = this.authenticationService.getLoggedInUserEmail();
            this.authenticationService.clearLoggedInUser();

            return this.reloginService.relogin(email).pipe(
                catchError(() => EMPTY), // only continue if relogin successful
                switchMap(() => this.retryRequest(request, next))
            );
        } else if (this.reloginService.isReloginInProgress()) {
            return this.reloginService.completed$.pipe(
                catchError(() => EMPTY), // only continue if relogin successful
                take(1),
                switchMap(() => this.retryRequest(request, next))
            );
        } else {
            this.router.navigateByUrl("/login");
            throw error;
        }
    }

    private retryRequest(request: HttpRequest<any>, next: HttpHandler) {
        return next.handle(request.clone());
    }

    private handle403(error: HttpErrorResponse): Observable<HttpEvent<any>> {
        this.router.navigateByUrl("/");
        throw error;
    }
}
