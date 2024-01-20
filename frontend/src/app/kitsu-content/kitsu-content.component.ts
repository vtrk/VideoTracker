import { Component } from '@angular/core';
import { KitsuContent } from '../utils/content';
import { ThemeService } from '../services/theme/theme.service';
import { ServerApiService } from '../services/server-api/server-api.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule, Location } from '@angular/common';
import {faSquarePlus, faSquareCheck, faSquareCaretRight, faEye, faTrashCan, faSquareXmark, faStop} from "@fortawesome/free-solid-svg-icons";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {CookieService} from "ngx-cookie-service";
import {AuthenticationService} from "../services/authentication/authentication.service";
import { Title } from '@angular/platform-browser';
import {FormControl, FormsModule, NgForm, ReactiveFormsModule} from "@angular/forms";
import { ReviewList } from '../utils/reviews';
import { strings } from '../utils/strings';
import { checkBan } from '../utils/check-ban';

@Component({
  selector: 'app-kitsu-content',
  standalone: true,
  imports: [CommonModule, FaIconComponent, FormsModule, ReactiveFormsModule],
  templateUrl: './kitsu-content.component.html',
  styleUrl: './kitsu-content.component.css'
})
export class KitsuContentComponent {
  content: KitsuContent
  reviews: ReviewList = new ReviewList();
  protected readonly faSquarePlus = faSquarePlus;
  protected readonly faSquareCheck = faSquareCheck;
  protected readonly faSquareCaretRight = faSquareCaretRight;
  protected readonly faEye = faEye;
  protected readonly faSquareXmark = faSquareXmark;
  protected readonly faStop = faStop;

  add_review: boolean = false;
  add_review_error_message: string = strings.add_review_error;

  remove_review: boolean = false;
  remove_review_error_message: string = strings.remove_review_error;

  input: FormControl;
  vote: string;

  CookieService: CookieService;

  constructor(public themeService: ThemeService, private route: ActivatedRoute, private api: ServerApiService, location: Location, router: Router, private cookieService: CookieService,private authServ: AuthenticationService, private title: Title) {
    this.input = new FormControl('');
    this.route.params.subscribe(params => { //Receives the request body as a stringified JSON object.
      checkBan(this.cookieService.get('id_user'), this.authServ, router , this.api, this.cookieService);
      location.replaceState('/content');
      let type = params['type'];
      let id = params['id'];
      this.content = new KitsuContent();
      this.api.getKitsuContent(this.content, type, id);
      this.api.getReview(id + "_" + type, this.reviews);
      this.CookieService = cookieService;
      this.title.setTitle("Content - Kitsu");
    });
  }

  loadReviews(): void {
    this.reviews = new ReviewList();
    this.api.getReview(this.content.id + "_" + this.content.type, this.reviews);
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

  showReview(){
    if(this.authServ.userIsAuth){
      let show: boolean = false;
      this.reviews.getReviewList().forEach(review => {
        if(review.id_user == this.cookieService.get('id_user'))
          show = true;
      });
      return show;
    }
    return !this.authServ.userIsAuth;
  }

  removeReview(event: any){
    this.remove_review = false;
    this.api.removeReview(this.cookieService.get('id_user'), this.content.id, this.content.type).subscribe({
      next: (data) => {
        let json = JSON.parse(JSON.stringify(data));
        if(json.response == '0')
          this.loadReviews();
        else
          this.remove_review = true;
      },
      error: (err) => {
        console.log(err);
        this.remove_review = true;
      },
    });
  }

  updateInput(event: any){
    this.input = new FormControl(event.target.value);
  }

  reload(){
    location.href = ('/kitsu/' + this.content.type + '/' + this.content.id);
  }

  onSubmit(form: NgForm){
    this.add_review = false;

    if(this.vote == undefined)
      this.vote = '0';
    this.api.addReview(this.content.id, this.content.type, this.vote, form.value.reviewText, this.content.title, this.content.episodeLength, this.content.episodeCount, this.content.poster).subscribe({
      next: (data) => {
        let json = JSON.parse(JSON.stringify(data));
        if(json.response == '0')
          this.loadReviews();
        else
          this.add_review = true;
      },
      error: (err) => {
        console.log(err);
        this.add_review = true;
      },
    });
  }

  onVoteChange(event: any){
    this.vote = event.target.options[event.target.options.selectedIndex].value;
  }

  protected readonly faTrashCan = faTrashCan;
}
