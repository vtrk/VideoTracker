import { Component } from '@angular/core';
import { KitsuContent } from '../utils/content';
import { ThemeService } from '../services/theme/theme.service';
import { ServerApiService } from '../services/server-api/server-api.service';
import { ActivatedRoute } from '@angular/router';
import { CommonModule, Location } from '@angular/common';
import {faSquarePlus, faSquareCheck, faSquareCaretRight, faEye, faTrashCan, faSquareXmark, faStop} from "@fortawesome/free-solid-svg-icons";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {CookieService} from "ngx-cookie-service";
import {AuthenticationService} from "../services/authentication/authentication.service";
import { Title } from '@angular/platform-browser';
import {FormControl, FormsModule, ReactiveFormsModule} from "@angular/forms";

@Component({
  selector: 'app-kitsu-content',
  standalone: true,
  imports: [CommonModule, FaIconComponent, FormsModule, ReactiveFormsModule],
  templateUrl: './kitsu-content.component.html',
  styleUrl: './kitsu-content.component.css'
})
export class KitsuContentComponent {
  content: KitsuContent
  protected readonly faSquarePlus = faSquarePlus;
  protected readonly faSquareCheck = faSquareCheck;
  protected readonly faSquareCaretRight = faSquareCaretRight;
  protected readonly faEye = faEye;
  protected readonly faSquareXmark = faSquareXmark;
  protected readonly faStop = faStop;

  input: FormControl;
  vote: string;
  CookieService: CookieService;

  constructor(public themeService: ThemeService, private route: ActivatedRoute, private api: ServerApiService, location: Location, cookieService: CookieService,private authServ: AuthenticationService, private title: Title) {
    this.route.params.subscribe(params => { //Receives the request body as a stringified JSON object.
      location.replaceState('/content');
      let type = params['type'];
      let id = params['id'];
      this.content = new KitsuContent();
      this.api.getKitsuContent(this.content, type, id);
      this.title.setTitle("Content - Kitsu");
      this.CookieService = cookieService;
    });
  }

  ngOnInit(): void {
    this.input = new FormControl('');
  }

  addPlanned(){
    this.api.addToList(this.content.id, 'plan to watch', this.content.title, this.content.episodeLength, this.content.episodeCount, this.content.poster, this.content.type);
  }

  addCompleted(){
    this.api.addToList(this.content.id, 'completed', this.content.title, this.content.episodeLength, this.content.episodeCount, this.content.poster, this.content.type);
  }

  addWatching(){
    this.api.addToList(this.content.id, 'watching', this.content.title, this.content.episodeLength, this.content.episodeCount, this.content.poster, this.content.type);
  }

  addDropped(){
    this.api.addToList(this.content.id, 'dropped', this.content.title, this.content.episodeLength, this.content.episodeCount, this.content.poster, this.content.type);
  }

  addOnHold(){
    this.api.addToList(this.content.id, 'on hold', this.content.title, this.content.episodeLength, this.content.episodeCount, this.content.poster, this.content.type);
  }

  show() {return !this.authServ.userIsAuth;}

  updateInput(event: any){
    this.input = new FormControl(event.target.value);
  }
  onSubmit(){
    this.api.addReview(this.content.id, this.content.type, this.vote, this.input.value);
  }

  onVoteChange(event: any){
    this.vote = event.target.options[event.target.options.selectedIndex].value;
  }

  getReviews(){
    this.api.getReview(this.content.id, this.content.type);
  }

  protected readonly faTrashCan = faTrashCan;
}
