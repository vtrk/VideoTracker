import { Component } from '@angular/core';
import { KitsuContent } from '../utils/content';
import { ThemeService } from '../theme.service';
import { ServerApiService } from '../services/server-api.service';
import { ActivatedRoute } from '@angular/router';
import { CommonModule, Location } from '@angular/common';

@Component({
  selector: 'app-kitsu-content',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './kitsu-content.component.html',
  styleUrl: './kitsu-content.component.css'
})
export class KitsuContentComponent {
  content: KitsuContent

  constructor(public themeService: ThemeService, private route: ActivatedRoute, private server: ServerApiService, location: Location) {
    this.route.params.subscribe(params => { //Receives the request body as a stringified JSON object.
      location.replaceState('/content');
      let type = params['type'];
      let id = params['id'];
      this.content = new KitsuContent();
      this.server.getKitsuContent(this.content, type, id);
    });
  }
}
