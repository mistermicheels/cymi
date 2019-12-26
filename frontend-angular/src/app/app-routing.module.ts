import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { AuthGuard } from "./core/auth-guard";
import { ConfirmEmailComponent } from "./pages/confirm-email/confirm-email.component";
import { EventCreateComponent } from "./pages/event-create/event-create.component";
import { EventComponent } from "./pages/event/event.component";
import { GroupAcceptInvitationComponent } from "./pages/group-accept-invitation/group-accept-invitation.component";
import { GroupCreateComponent } from "./pages/group-create/group-create.component";
import { GroupComponent } from "./pages/group/group.component";
import { HomeComponent } from "./pages/home/home.component";
import { LoginComponent } from "./pages/login/login.component";
import { MyAccountComponent } from "./pages/my-account/my-account.component";
import { SignupComponent } from "./pages/signup/signup.component";

const routes: Routes = [
    { path: "confirm-email/token/:token", component: ConfirmEmailComponent },
    { path: "event/:eventId", component: EventComponent, canActivate: [AuthGuard] },
    {
        path: "event-create/group/:groupId",
        component: EventCreateComponent,
        canActivate: [AuthGuard]
    },
    { path: "group/:groupId", component: GroupComponent, canActivate: [AuthGuard] },
    {
        path: "group/:groupId/accept-invitation",
        component: GroupAcceptInvitationComponent,
        canActivate: [AuthGuard]
    },
    { path: "group-create", component: GroupCreateComponent, canActivate: [AuthGuard] },
    { path: "login", component: LoginComponent },
    { path: "my-account", component: MyAccountComponent, canActivate: [AuthGuard] },
    { path: "signup", component: SignupComponent },
    { path: "signup/token/:token", component: SignupComponent },
    { path: "", component: HomeComponent, canActivate: [AuthGuard] }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {}
