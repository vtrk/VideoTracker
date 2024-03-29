import { Component } from '@angular/core';
import {NgClass} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {ThemeService} from "../services/theme/theme.service";
import {ServerApiService} from "../services/server-api/server-api.service";
import {HttpClient} from "@angular/common/http";
import { UserData } from '../utils/user-data';
import { Title } from '@angular/platform-browser';

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
  email: string;
  username: string;
  completed: string;
  watching: string;
  stopped: string;
  planned: string;
  info: Array<string>;
  user: UserData = new UserData();


  constructor(private api: ServerApiService, public themeService: ThemeService, private client: HttpClient, private title: Title) {
    this.title.setTitle("Profile");
    this.api.getInfoProfile(this.user);
  }

}
