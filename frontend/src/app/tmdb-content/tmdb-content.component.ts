import { Component } from '@angular/core';
import { ServerApiService } from '../services/server-api-service/server-api.service';
import { ThemeService } from '../theme.service';
import { ActivatedRoute } from '@angular/router';
import { TMDBContent } from '../utils/content';
import { CommonModule, Location } from '@angular/common';
import {faEye, faSquareCaretRight, faSquareCheck, faSquarePlus} from "@fortawesome/free-solid-svg-icons";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {AuthenticationService} from "../services/authentication-service/authentication.service";

@Component({
  selector: 'app-tmdb-content',
  standalone: true,
  imports: [CommonModule, FaIconComponent],
  templateUrl: './tmdb-content.component.html',
  styleUrl: './tmdb-content.component.css'
})
export class TmdbContentComponent {
  content: TMDBContent
  protected readonly faEye = faEye;
  protected readonly faSquareCaretRight = faSquareCaretRight;
  protected readonly faSquareCheck = faSquareCheck;
  protected readonly faSquarePlus = faSquarePlus;

  constructor(public themeService: ThemeService, private route: ActivatedRoute, private api: ServerApiService, location: Location, private authServ: AuthenticationService) {
    this.route.params.subscribe(params => { //Receives the request body as a stringified JSON object.
      location.replaceState('/content');
      let type = params['type'];
      let id = params['id'];
      this.content = new TMDBContent();
      api.getTMDBContent(this.content, type, id);
    });
  }

  addPlanned(){
    this.api.addToList(this.content.id, 'planned', this.content.title, this.content.runtime, this.content.episodes, this.content.poster, this.content.type);
  }

  addCompleted(){
    this.api.addToList(this.content.id, 'completed', this.content.title, this.content.runtime, this.content.episodes, this.content.poster, this.content.type);
  }

  addWatching(){
    this.api.addToList(this.content.id, 'watching', this.content.title, this.content.runtime, this.content.episodes, this.content.poster, this.content.type);
  }

  addStopped(){
    this.api.addToList(this.content.id, 'on-hold', this.content.title, this.content.runtime, this.content.episodes, this.content.poster, this.content.type);
  }

  show() {return !this.authServ.userIsAuth;}

}
