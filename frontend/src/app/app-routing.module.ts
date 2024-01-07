import { NgModule } from "@angular/core";
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './home/home.component';
import { SettingsComponent } from './settings/settings.component';
import {UserListComponent} from "./user-list/user-list.component";
import {AboutComponent} from "./about/about.component";
import { LoginComponent } from "./login/login.component";



export const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full'},
  { path: 'home', component: HomeComponent },
  { path: 'settings', component: SettingsComponent },
  { path: 'about', component: AboutComponent },
  { path: 'user-list', component: UserListComponent },
  { path: 'login', component: LoginComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
