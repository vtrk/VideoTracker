import { Component } from '@angular/core';
import { ServerApiService } from '../services/server-api/server-api.service';
import { ThemeService } from '../services/theme/theme.service';
import { ActivatedRoute } from '@angular/router';
import { TMDBContent } from '../utils/content';
import { CommonModule, Location } from '@angular/common';
import {
    faEye,
    faSquareCaretRight,
    faSquareCheck,
    faSquarePlus,
    faSquareXmark,
    faStop,
    faTrashCan
} from "@fortawesome/free-solid-svg-icons";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {AuthenticationService} from "../services/authentication/authentication.service";
import {ReviewList} from "../utils/reviews";
import {FormControl} from "@angular/forms";
import { CookieService } from 'ngx-cookie-service';

@Component({
  selector: 'app-tmdb-content',
  standalone: true,
  imports: [CommonModule, FaIconComponent],
  templateUrl: './tmdb-content.component.html',
  styleUrl: './tmdb-content.component.css'
})
export class TmdbContentComponent {
  content: TMDBContent
  reviews: ReviewList = new ReviewList();
  protected readonly faEye = faEye;
  protected readonly faSquareCaretRight = faSquareCaretRight;
  protected readonly faSquareCheck = faSquareCheck;
  protected readonly faSquarePlus = faSquarePlus;
  protected readonly faSquareXmark = faSquareXmark;
  protected readonly faStop = faStop;

  input: FormControl;
  vote: string;

  CookieService: CookieService;

  constructor(public themeService: ThemeService, private route: ActivatedRoute, private api: ServerApiService, location: Location, private authServ: AuthenticationService, private cookieService: CookieService) {
    this.route.params.subscribe(params => { //Receives the request body as a stringified JSON object.
      location.replaceState('/content');
      let type = params['type'];
      let id = params['id'];
      this.content = new TMDBContent();
      api.getTMDBContent(this.content, type, id);
      api.getReview(id + "_" + type, this.reviews);
      this.CookieService = cookieService;
    });
  }
  ngOnInit(): void {
    this.input = new FormControl('');
  }
  addPlanned(){
    this.api.addToList(this.content.id, 'plan to watch', this.content.title, this.content.runtime, this.content.episodes, this.content.poster, this.content.type);
  }

  addCompleted(){
    this.api.addToList(this.content.id, 'completed', this.content.title, this.content.runtime, this.content.episodes, this.content.poster, this.content.type);
  }

  addWatching(){
    this.api.addToList(this.content.id, 'watching', this.content.title, this.content.runtime, this.content.episodes, this.content.poster, this.content.type);
  }

  addDropped(){
    this.api.addToList(this.content.id, 'dropped', this.content.title, this.content.runtime, this.content.episodes, this.content.poster, this.content.type);
  }

  addOnHold(){
    this.api.addToList(this.content.id, 'on hold', this.content.title, this.content.runtime, this.content.episodes, this.content.poster, this.content.type);
  }

  show() {return !this.authServ.userIsAuth;}


  showReview(){
    if(this.authServ.userIsAuth){
      let show: boolean = false;
      this.reviews.getReviewList().forEach(review => {
        if(review.id_user == this.CookieService.get('id_user'))
          show = true;
      });
      return show;
    }
    return !this.authServ.userIsAuth;
  }

  updateInput(event: any){
    this.input = new FormControl(event.target.value);
  }
  onSubmit(){
    this.api.addReview(this.content.id, this.content.type, this.vote, this.input.value, this.content.title, this.content.runtime, this.content.episodes, this.content.poster);
  }

  onVoteChange(event: any){
    this.vote = event.target.options[event.target.options.selectedIndex].value;
  }
    protected readonly faTrashCan = faTrashCan;
}
