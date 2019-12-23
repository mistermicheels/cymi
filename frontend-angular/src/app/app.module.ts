import { HTTP_INTERCEPTORS, HttpClientModule, HttpClientXsrfModule } from "@angular/common/http";
import { ErrorHandler, NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { BrowserModule } from "@angular/platform-browser";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { BootstrapVersion, NgBootstrapFormValidationModule } from "ng-bootstrap-form-validation";
import { CookieService } from "ngx-cookie-service";

import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { DefaultErrorHandler } from "./core/default-error-handler";
import { AuthInterceptor } from "./core/interceptors/auth-interceptor";
import { AuthenticationService } from "./core/services/authentication.service";
import { CurrentUserService } from "./core/services/current-user.service";
import { ReloginService } from "./core/services/relogin.service";
import { ConfirmEmailComponent } from "./pages/confirm-email/confirm-email.component";
import { HomeComponent } from "./pages/home/home.component";
import { LoginComponent } from "./pages/login/login.component";
import { MyAccountComponent } from "./pages/my-account/my-account.component";
import { SignupComponent } from "./pages/signup/signup.component";
import { ReloginModalComponent } from "./shared/relogin-modal/relogin-modal.component";

@NgModule({
    declarations: [
        AppComponent,
        HomeComponent,
        LoginComponent,
        SignupComponent,
        ReloginModalComponent,
        ConfirmEmailComponent,
        MyAccountComponent
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        AppRoutingModule,
        HttpClientModule,
        HttpClientXsrfModule.withOptions({
            cookieName: "XSRF-TOKEN",
            headerName: "X-XSRF-TOKEN"
        }),
        FormsModule,
        ReactiveFormsModule,

        // both are needed here because we only use a single module
        NgBootstrapFormValidationModule.forRoot({ bootstrapVersion: BootstrapVersion.Four }),
        NgBootstrapFormValidationModule,

        NgbModule
    ],
    providers: [
        CookieService,
        { provide: ErrorHandler, useClass: DefaultErrorHandler },
        { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
        AuthenticationService,
        CurrentUserService,
        ReloginService
    ],
    bootstrap: [AppComponent],
    entryComponents: [ReloginModalComponent]
})
export class AppModule {}
