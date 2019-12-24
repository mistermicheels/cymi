import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { CookieService } from "ngx-cookie-service";
import { BehaviorSubject } from "rxjs";
import { tap } from "rxjs/operators";

import { ApiUser } from "../api-models/ApiUser";

import { CurrentUserService } from "./current-user.service";

@Injectable({
    providedIn: "root"
})
export class AuthenticationService {
    private readonly API_PATH = "/api/authentication";

    private loggedInUser?: ApiUser;

    private isUserLoggedInSubject = new BehaviorSubject<boolean>(false);
    isUserLoggedIn$ = this.isUserLoggedInSubject.asObservable();

    constructor(
        private http: HttpClient,
        private cookieService: CookieService,
        private currentUserService: CurrentUserService
    ) {}

    initializeLoggedInUser() {
        const tokenFound = this.cookieService.check("XSRF-TOKEN");

        if (tokenFound) {
            this.currentUserService.getCurrentUser().subscribe(
                user => this.setLoggedInUser(user),
                () => this.clearLoggedInUser()
            );
        } else {
            this.clearLoggedInUser();
        }
    }

    private setLoggedInUser(user: ApiUser) {
        this.loggedInUser = user;
        this.isUserLoggedInSubject.next(true);
    }

    clearLoggedInUser() {
        this.loggedInUser = undefined;
        this.isUserLoggedInSubject.next(false);
    }

    isUserLoggedIn() {
        return this.isUserLoggedInSubject.value;
    }

    getLoggedInUserEmail() {
        if (!this.loggedInUser) {
            throw new Error(
                "Cannot get email for logged in user because there is no logged in user"
            );
        }

        return this.loggedInUser.email;
    }

    getLoggedInUserDefaultDisplayName() {
        if (!this.loggedInUser) {
            throw new Error(
                "Cannot get default display name for logged in user because there is no logged in user"
            );
        }

        return this.loggedInUser.defaultDisplayName;
    }

    logIn(email: string, password: string) {
        return this.http
            .post<ApiUser>(this.API_PATH + "/log-in", { email, password })
            .pipe(tap(user => this.setLoggedInUser(user)));
    }

    logOut() {
        return this.http
            .post<void>(this.API_PATH + "/log-out", {})
            .pipe(tap(() => this.clearLoggedInUser()));
    }

    signUp(email: string, password: string, defaultDisplayName: string) {
        return this.http.post<void>(this.API_PATH + "/sign-up", {
            email,
            password,
            defaultDisplayName
        });
    }

    confirmEmail(token: string, userId: number) {
        return this.http.post<void>(this.API_PATH + "/confirm-email", {
            token,
            userId
        });
    }
}
