import { Component } from '@angular/core';
import { ServerApiService } from '../services/server-api.service';
import { ThemeService } from '../theme.service';
import { ActivatedRoute } from '@angular/router';
import { TMDBContent } from '../utils/content';
import { CommonModule } from '@angular/common';
import { Location } from '@angular/common';

@Component({
  selector: 'app-tmdb-content',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './tmdb-content.component.html',
  styleUrl: './tmdb-content.component.css'
})
export class TmdbContentComponent {
  content: TMDBContent

  constructor(public themeService: ThemeService, private route: ActivatedRoute, private server: ServerApiService, location: Location) {
    this.route.params.subscribe(params => { //Receives the request body as a stringified JSON object.
      location.replaceState('/content');
      let type = params['type'];
      let id = params['id'];
      this.content = new TMDBContent();
      server.getTMDBContent(this.content, type, id);
    });
  }
}
