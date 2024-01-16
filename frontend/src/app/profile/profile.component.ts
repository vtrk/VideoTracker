import { Component } from '@angular/core';
import {NgClass} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {ThemeService} from "../theme.service";
import {ServerApiService} from "../services/server-api.service";
import {HttpClient} from "@angular/common/http";

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

  constructor(private api: ServerApiService, public themeService: ThemeService, private client: HttpClient) {
    this.info = this.api.getInfoProfile();
    this.email = this.info[0];
    this.username = this.info[1];
    this.completed = this.info[2];
    this.watching = this.info[3];
    this.stopped = this.info[4];
    this.planned = this.info[5];
  }

}
