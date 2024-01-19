import { Component } from '@angular/core';
import { KitsuContent } from '../utils/content';
import { ThemeService } from '../theme.service';
import { ServerApiService } from '../services/server-api-service/server-api.service';
import { ActivatedRoute } from '@angular/router';
import { CommonModule, Location } from '@angular/common';
import {faSquarePlus, faSquareCheck, faSquareCaretRight, faEye, faTrashCan} from "@fortawesome/free-solid-svg-icons";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {CookieService} from "ngx-cookie-service";
import {AuthenticationService} from "../services/authentication-service/authentication.service";
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-kitsu-content',
  standalone: true,
  imports: [CommonModule, FaIconComponent],
  templateUrl: './kitsu-content.component.html',
  styleUrl: './kitsu-content.component.css'
})
export class KitsuContentComponent {
  content: KitsuContent
  protected readonly faSquarePlus = faSquarePlus;
  protected readonly faSquareCheck = faSquareCheck;
  protected readonly faSquareCaretRight = faSquareCaretRight;
  protected readonly faEye = faEye;

  constructor(public themeService: ThemeService, private route: ActivatedRoute, private api: ServerApiService, location: Location, cookieService: CookieService,private authServ: AuthenticationService, private title: Title) {
    this.route.params.subscribe(params => { //Receives the request body as a stringified JSON object.
      location.replaceState('/content');
      let type = params['type'];
      let id = params['id'];
      this.content = new KitsuContent();
      this.api.getKitsuContent(this.content, type, id);
      this.title.setTitle("Content - Kitsu");
    });
  }

  addPlanned(){
    this.api.addToList(this.content.id, 'planned', this.content.title, this.content.episodeLength, this.content.episodeCount, this.content.poster, this.content.type);
  }

  addCompleted(){
    this.api.addToList(this.content.id, 'completed', this.content.title, this.content.episodeLength, this.content.episodeCount, this.content.poster, this.content.type);
  }

  addWatching(){
    this.api.addToList(this.content.id, 'watching', this.content.title, this.content.episodeLength, this.content.episodeCount, this.content.poster, this.content.type);
  }

  addStopped(){
    this.api.addToList(this.content.id, 'dropped', this.content.title, this.content.episodeLength, this.content.episodeCount, this.content.poster, this.content.type);
  }

  show() {return !this.authServ.userIsAuth;}

  protected readonly faTrashCan = faTrashCan;
}
