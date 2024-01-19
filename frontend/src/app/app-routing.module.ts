import { NgModule } from "@angular/core";
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from "./guard/auth.guard";

import { HomeComponent } from './home/home.component';
import { SettingsComponent } from './settings/settings.component';
import { UserListComponent} from "./user-list/user-list.component";
import { LoginComponent } from "./login/login.component";
import { SigninComponent } from "./signin/signin.component";
import { SearchresultsComponent } from "./searchresults/searchresults.component";
import { MailboxComponent } from "./mailbox/mailbox.component";
import { ResetPasswordComponent } from "./reset-password/reset-password.component";
import { TmdbContentComponent } from "./tmdb-content/tmdb-content.component";
import { KitsuContentComponent } from "./kitsu-content/kitsu-content.component";
import { ProfileComponent } from "./profile/profile.component";

export const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full'},
  { path: 'home', component: HomeComponent },
  { path: 'settings', component: SettingsComponent, canActivate: [AuthGuard]},
  { path: 'mailbox', component: MailboxComponent, canActivate: [AuthGuard] },
  { path: 'user-list', component: UserListComponent, canActivate: [AuthGuard] },
  { path: 'signin', component: SigninComponent },
  { path: 'login', component: LoginComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'resetpassword', component: ResetPasswordComponent },
  { path: 'search/:searchQuery', component: SearchresultsComponent },
  { path: 'search', redirectTo: '/home', pathMatch: 'full' }, // Redirects to home if no search query is given.
  { path: 'tmdb/:type/:id', component: TmdbContentComponent}, // Displays the content for the TMDB API.
  { path: 'content', redirectTo: '/home', pathMatch: 'full' }, // Redirects to home if no content is given.
  { path: 'kitsu/:type/:id', component: KitsuContentComponent }, // Displays the content for the Kitsu API.
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: []
})
export class AppRoutingModule { }
