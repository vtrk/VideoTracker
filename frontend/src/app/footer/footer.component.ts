import { Component } from '@angular/core';
import {RouterLink} from "@angular/router";
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faGithub } from '@fortawesome/free-brands-svg-icons';
import { faX } from '@fortawesome/free-solid-svg-icons';
import { ThemeService } from "../services/theme/theme.service";
import { NgClass } from "@angular/common";
import { strings } from '../utils/strings';
import { ServerApiService } from '../services/server-api/server-api.service';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [
    RouterLink,
    FontAwesomeModule,
    NgClass
  ],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.css'
})
export class FooterComponent {
  faGithub = faGithub;
  faX = faX;
  client_version = strings.CLIENT_VERSION;
  server_version = '';
  server_api = '';


  constructor(public themeService: ThemeService, private server: ServerApiService) {
    this.server.getServerInfoObservable().subscribe(
      (data) => {
        this.server_version = data.Version;
        this.server_api = data.API;
      }
    );
  }
}
