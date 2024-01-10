import { Component } from '@angular/core';
import { ServerApiService } from '../services/server-api.service';
import { strings } from '../strings';
import {ThemeService} from "../theme.service";
import {NgClass} from "@angular/common";

@Component({
  selector: 'app-about',
  standalone: true,
  imports: [
    NgClass
  ],
  templateUrl: './about.component.html',
  styleUrl: './about.component.css'
})
export class AboutComponent {
  title = 'about';
  about_serverAPI: string = strings.about_serverAPI;
  about_serverVersion: string = strings.about_serverVersion;
  API_Info: Map<string, string> = new Map<string, string>;

  constructor(private server: ServerApiService, public themeService: ThemeService) {
    this.server.getServerInfo(this.API_Info);
  }
}
