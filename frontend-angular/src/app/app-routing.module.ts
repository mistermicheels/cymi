import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { ConfirmEmailComponent } from "./pages/confirm-email/confirm-email.component";
import { HomeComponent } from "./pages/home/home.component";
import { LoginComponent } from "./pages/login/login.component";
import { MyAccountComponent } from "./pages/my-account/my-account.component";
import { SignupComponent } from "./pages/signup/signup.component";

const routes: Routes = [
    { path: "confirm-email/token/:token/user-id/:userId", component: ConfirmEmailComponent },
    { path: "login", component: LoginComponent },
    { path: "my-account", component: MyAccountComponent },
    { path: "signup", component: SignupComponent },
    { path: "", component: HomeComponent }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {}
