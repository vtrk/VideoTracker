import { Component, OnInit } from '@angular/core';
import { ItemList } from '../utils/item';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ServerApiService } from '../services/server-api.service';
import { SearchBody } from '../utils/data-structures';
import { Location } from '@angular/common';

@Component({
  selector: 'app-searchresults',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './searchresults.component.html',
  styleUrl: './searchresults.component.css'
})
export class SearchresultsComponent implements OnInit{
  title = 'Search Results';
  searchQuery: string = '';
  itemList: ItemList;
  
  constructor(private route: ActivatedRoute, private server: ServerApiService, location: Location) {
    location.replaceState('/search');
    this.route.params.subscribe(params => { //Recieves the request body as a stringified JSON object.
      let body = JSON.parse(params['searchQuery']); //Converts the stringified JSON object into a SearchBody.
      this.itemList = new ItemList();
      this.server.getSearch(this.itemList, new SearchBody(body.type, body.query, body.args));
    });
  }

  ngOnInit(): void {
  }

  goto(event: any){
    var target = event.target;
    var value = target.attributes.id.nodeValue;
    var type = target.attributes.title.nodeValue;
    console.log(value);
    console.log(type);
  }
}
