import { Component } from '@angular/core';
import {NgClass} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {ThemeService} from "../theme.service";
import {ServerApiService} from "../services/server-api.service";

@Component({
  selector: 'app-profile',
  standalone: true,
    imports: [
        FormsModule,
        NgClass
    ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {

  info: Array<string>;

  constructor(private api: ServerApiService, public themeService: ThemeService) {}

  getInfo(){
     this.info = this.api.getInfoProfile();
  }
}
