import { NgModule } from "@angular/core";
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './home/home.component';
import { ContentComponent } from './content/content.component';
import {UserListComponent} from "./user-list/user-list.component";



export const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'content', component: ContentComponent },
  { path: 'user-list', component: UserListComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
