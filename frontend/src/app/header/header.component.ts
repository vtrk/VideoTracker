import { Component, OnInit } from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import { HttpClient } from '@angular/common/http';
import { FormControl, FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { ServerApiService } from '../services/server-api.service';
import { SearchBody } from '../utils/data-structures';
import { CommonModule } from '@angular/common';
import { strings } from '../strings';
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {
  faBookmark,
  faDoorClosed,
  faDoorOpen,
  faGear,
  faHouse,
  faMagnifyingGlass, faMoon,
  faRightToBracket, faSun,
  faEnvelope,faUser
} from '@fortawesome/free-solid-svg-icons';
import {AuthenticationService} from "../authentication.service";
import {ThemeService} from "../theme.service";

@Component({
  selector: 'app-header',
  standalone: true,
    imports: [
        RouterLink,
        FormsModule,
        ReactiveFormsModule,
        CommonModule,
        FontAwesomeModule
    ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit{

  protected readonly faHouse = faHouse;
  protected readonly faMagnifyingGlass = faMagnifyingGlass;
  protected readonly faDoorClosed = faDoorClosed;
  protected readonly faRightToBracket = faRightToBracket;
  protected readonly faBookmark = faBookmark;
  protected readonly faGear = faGear;
  protected readonly faDoorOpen = faDoorOpen;
  protected readonly faSun = faSun;
  protected readonly faMoon = faMoon;
  protected readonly faEnvelope = faEnvelope;
  protected readonly faUser = faUser;

  input: FormControl;
  type: string;
  year: string;
  category: string;
  range: number[] = [];
  startYear: number = new Date().getFullYear();

  searchQuery: string = '';
  serverInfo: Map<string, string> = new Map<string, string>();

  constructor(private http: HttpClient, private router: Router, private server: ServerApiService, private authService: AuthenticationService, public themeService: ThemeService) {
    this.server.getServerInfo(this.serverInfo);
  }

  isUserLoggedIn(): boolean {
    return this.authService.userIsAuth;
  }

  logout(){
    this.authService.logout();
    this.router.navigate(['/home']);
  }

  ngOnInit(): void{
    this.input = new FormControl('');
    this.type = "movie";
    this.year = "none";
    this.category = "none";
    for (let i = 0; i < 99; i++) {
      this.range.push(this.startYear - i);
    }
  }

  onSubmit(){

    //Avoids queries with '&' since it could disrupt the external API calls.
    if(!this.input.value.includes('&')){
      switch(this.serverInfo.get('API')){ //Using SearchBody it is possible to get the JSON body of the request to send to the other
        case strings.KITSU:
          if(this.category == 'none')
            this.router.navigate(['/search', new SearchBody(strings.anime, this.input.value, new Map<string, string>()).getJSONBody()]);
          else
            this.router.navigate(['/search', new SearchBody(strings.anime, this.input.value, new Map<string, string>([['categories', this.category]])).getJSONBody()]);
          break;
        case strings.TMDB:
          if(this.year === 'none')
            this.router.navigate(['/search', new SearchBody(this.type, this.input.value, new Map<string, string>()).getJSONBody()]);
          else
            switch(this.type){
              case strings.movie:
                this.router.navigate(['/search', new SearchBody(this.type, this.input.value, new Map<string, string>([[strings.movie_year, this.year]])).getJSONBody()]);
                break;
              case strings.tv:
                this.router.navigate(['/search', new SearchBody(this.type, this.input.value, new Map<string, string>([[strings.tv_year, this.year]])).getJSONBody()]);
                break;
              default:
                console.log(strings.API_ERROR);
            }
          break;
        default:
          console.log(strings.API_ERROR);
      }
    }
  }

  onTMDBTypeChange(event: any){
    this.type = event.target.options[event.target.options.selectedIndex].value
  }

  onTMDBYearChange(event: any){
    this.year = event.target.options[event.target.options.selectedIndex].value
  }

  onKitsuCategoryChange(event: any){
    this.category = event.target.options[event.target.options.selectedIndex].value
  }

  /**
   * Checks if the API name is the same as the string passed.
   * @param api name of the api to check
   * @returns the result of the comparison between the string passed and the API name, if the API name is not set returns true
   */
  apiIs(api: string): boolean{
    let realAPI = this.serverInfo.get('API');
    if(realAPI == strings.API_ERROR)
      return true;

    return realAPI == api;
  }
}
