import { Component } from '@angular/core';
import { ItemList } from '../utils/item';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ServerApiService } from '../services/server-api-service/server-api.service';
import { SearchBody } from '../utils/data-structures';
import { Location } from '@angular/common';
import { ItemListComponent } from '../item-list/item-list.component';
import {ThemeService} from "../services/theme/theme.service";

@Component({
  selector: 'app-searchresults',
  standalone: true,
  imports: [CommonModule, ItemListComponent],
  templateUrl: './searchresults.component.html',
  styleUrl: './searchresults.component.css'
})
export class SearchresultsComponent{
  title = 'Search Results';
  searchQuery: string = '';
  itemList: ItemList;

  constructor(public themeService: ThemeService, private route: ActivatedRoute, private server: ServerApiService, location: Location) {
    this.route.params.subscribe(params => { //Receives the request body as a stringified JSON object.
      location.replaceState('/search');
      let body = JSON.parse(params['searchQuery']); //Converts the stringified JSON object into a SearchBody.
      this.itemList = new ItemList();
      this.server.getSearch(this.itemList, new SearchBody(body.type, body.query, new Map(Object.entries(body.args))));
    });
  }
}
