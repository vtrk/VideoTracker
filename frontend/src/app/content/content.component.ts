import { Component } from '@angular/core';
import { Content, TMDBContent } from '../utils/content';
import { ThemeService } from '../theme.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ServerApiService } from '../services/server-api.service';
import { TmdbContentComponent } from "../tmdb-content/tmdb-content.component";
import { strings } from '../strings';

@Component({
    selector: 'app-content',
    standalone: true,
    templateUrl: './content.component.html',
    styleUrl: './content.component.css',
    imports: [TmdbContentComponent]
})
export class ContentComponent {
  content: Content
  contentType: string

  constructor(public themeService: ThemeService, private route: ActivatedRoute, private router: Router) {
    this.route.params.subscribe(params => { //Receives the request body as a stringified JSON object.
      let type = params['type'];
      let id = params['id'];

      switch(type){
        case strings.movie:
        case strings.tv:
          this.router.navigate(['/tmdb', type, id]);
          break;
        case strings.anime:
          //TODO
          break;
        default:
          break;
      }
    });
  }

  getContentTMDB(): TMDBContent {
    console.log(this.content);
    return this.content as TMDBContent;
  }
}
