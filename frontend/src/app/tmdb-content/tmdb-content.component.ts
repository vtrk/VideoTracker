import { Component } from '@angular/core';
import { ServerApiService } from '../services/server-api.service';
import { ThemeService } from '../theme.service';
import { ActivatedRoute } from '@angular/router';
import { TMDBContent } from '../utils/content';
import { CommonModule, Location } from '@angular/common';
import {faEye, faSquareCaretRight, faSquareCheck, faSquarePlus} from "@fortawesome/free-solid-svg-icons";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";

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

  constructor(public themeService: ThemeService, private route: ActivatedRoute, private server: ServerApiService, location: Location) {
    this.route.params.subscribe(params => { //Receives the request body as a stringified JSON object.
      location.replaceState('/content');
      let type = params['type'];
      let id = params['id'];
      this.content = new TMDBContent();
      server.getTMDBContent(this.content, type, id);
    });
  }

  addPlanned(){

  }

  addCompleted(){

  }

  addWatching(){

  }

  addStopped(){

  }

}
