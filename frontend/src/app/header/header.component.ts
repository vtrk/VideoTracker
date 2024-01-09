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
import { faGear } from '@fortawesome/free-solid-svg-icons';
import {faGithub} from "@fortawesome/free-brands-svg-icons";

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
    faGear = faGear;
    input: FormControl;
    type: string;
    year: string;
    category: string;

    range: number[] = [];
    startYear: number = new Date().getFullYear();

    searchQuery: string = '';
    serverInfo: Map<string, string> = new Map<string, string>();

    constructor(private http: HttpClient, private router: Router, private server: ServerApiService) {
      this.server.getServerInfo(this.serverInfo);
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
          case 'Kitsu':
            if(this.category == 'none')
              this.router.navigate(['/search', new SearchBody(strings.anime, this.input.value, new Map<string, string>()).getJSONBody()]);
            else
              this.router.navigate(['/search', new SearchBody(strings.anime, this.input.value, new Map<string, string>([['category', this.category]])).getJSONBody()]);
            break;
          case 'TMDB':
            //TODO: implement
            console.log(this.year);
            console.log(this.type);
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

    protected readonly faGithub = faGithub;
  }
