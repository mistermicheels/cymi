import { HTTP_INTERCEPTORS, HttpClientModule, HttpClientXsrfModule } from "@angular/common/http";
import { ErrorHandler, NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { BrowserModule } from "@angular/platform-browser";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { FlatpickrModule } from "angularx-flatpickr";
import { BootstrapVersion, NgBootstrapFormValidationModule } from "ng-bootstrap-form-validation";
import { CookieService } from "ngx-cookie-service";

import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { DefaultErrorHandler } from "./core/default-error-handler";
import { AutofocusDirective } from "./core/directives/autofocus.directive";
import { AuthInterceptor } from "./core/interceptors/auth-interceptor";
import { AuthenticationService } from "./core/services/authentication.service";
import { CurrentUserService } from "./core/services/current-user.service";
import { EventsService } from "./core/services/events.service";
import { GroupsService } from "./core/services/groups.service";
import { ReloginService } from "./core/services/relogin.service";
import { ConfirmEmailComponent } from "./pages/confirm-email/confirm-email.component";
import { EventCreateComponent } from "./pages/event-create/event-create.component";
import { EventComponent } from "./pages/event/event.component";
import { GroupAcceptInvitationComponent } from "./pages/group-accept-invitation/group-accept-invitation.component";
import { GroupCreateComponent } from "./pages/group-create/group-create.component";
import { GroupComponent } from "./pages/group/group.component";
import { GroupsComponent } from "./pages/home/groups/groups.component";
import { HomeComponent } from "./pages/home/home.component";
import { LoginComponent } from "./pages/login/login.component";
import { MyAccountComponent } from "./pages/my-account/my-account.component";
import { SignupComponent } from "./pages/signup/signup.component";
import { EventCardComponent } from "./shared/event-card/event-card.component";
import { EventOtherStatusesComponent } from "./shared/event-other-statuses/event-other-statuses.component";
import { EventResponseStatusComponent } from "./shared/event-response-status/event-response-status.component";
import { ReloginModalComponent } from "./shared/relogin-modal/relogin-modal.component";
import { UserRoleIndicatorComponent } from "./shared/user-role-indicator/user-role-indicator.component";

@NgModule({
    declarations: [
        AutofocusDirective,
        AppComponent,
        HomeComponent,
        LoginComponent,
        SignupComponent,
        ReloginModalComponent,
        ConfirmEmailComponent,
        MyAccountComponent,
        GroupsComponent,
        GroupComponent,
        GroupCreateComponent,
        GroupAcceptInvitationComponent,
        EventCreateComponent,
        EventComponent,
        EventCardComponent,
        EventOtherStatusesComponent,
        EventResponseStatusComponent,
        UserRoleIndicatorComponent
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

        NgbModule,
        FlatpickrModule.forRoot({
            allowInput: false,
            altFormat: "F j, Y, H:i",
            altInput: true,
            altInputClass: "form-control",
            dateFormat: "Z",
            time24hr: true
        }),
        FontAwesomeModule
    ],
    providers: [
        CookieService,
        { provide: ErrorHandler, useClass: DefaultErrorHandler },
        { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
        AuthenticationService,
        CurrentUserService,
        ReloginService,
        GroupsService,
        EventsService
    ],
    bootstrap: [AppComponent],
    entryComponents: [ReloginModalComponent]
})
export class AppModule {}
