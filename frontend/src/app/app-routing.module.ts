import { NgModule } from "@angular/core";
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './home/home.component';
import { SettingsComponent } from './settings/settings.component';
import {UserListComponent} from "./user-list/user-list.component";
import {AboutComponent} from "./about/about.component";
import { LoginComponent } from "./login/login.component";
import { SigninComponent } from "./signin/signin.component";
import { SearchService } from "./search.service";
import { SearchresultsComponent } from "./searchresults/searchresults.component";

export const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full'},
  { path: 'home', component: HomeComponent },
  { path: 'settings', component: SettingsComponent },
  { path: 'about', component: AboutComponent },
  { path: 'user-list', component: UserListComponent },
  { path: 'signin', component: SigninComponent },
  { path: 'login', component: LoginComponent },
  { path: 'search/:searchQuery', component: SearchresultsComponent },
  { path: 'search', redirectTo: '/home', pathMatch: 'full' } // Redirects to home if no search query is given.
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [SearchService]
})
export class AppRoutingModule { }
