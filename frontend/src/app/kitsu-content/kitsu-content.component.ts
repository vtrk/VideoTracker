import { Component } from '@angular/core';
import { KitsuContent } from '../utils/content';
import { ThemeService } from '../theme.service';
import { ServerApiService } from '../services/server-api.service';
import { ActivatedRoute } from '@angular/router';
import { CommonModule, Location } from '@angular/common';
import {faSquarePlus,faSquareCheck, faSquareCaretRight, faEye} from "@fortawesome/free-solid-svg-icons";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {CookieService} from "ngx-cookie-service";

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

  constructor(public themeService: ThemeService, private route: ActivatedRoute, private api: ServerApiService, location: Location, cookieService: CookieService) {
    this.route.params.subscribe(params => { //Receives the request body as a stringified JSON object.
      location.replaceState('/content');
      let type = params['type'];
      let id = params['id'];
      this.content = new KitsuContent();
      this.api.getKitsuContent(this.content, type, id);
    });
  }

  addPlanned(){
    this.api.addToPlanned(this.content.id);
  }

  addCompleted(){
    this.api.addToCompleted(this.content.id);
  }

  addWatching(){
    this.api.addToWatching(this.content.id);
  }

  addStopped(){
    this.api.addToStopped(this.content.id);
  }

}
