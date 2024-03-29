import { Component, OnInit } from '@angular/core';
import { ItemList, } from '../utils/item';
import { ServerApiService } from '../services/server-api/server-api.service';
import { CommonModule } from '@angular/common';
import {ThemeService} from "../services/theme/theme.service";
import { ItemListComponent } from '../item-list/item-list.component';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, ItemListComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit{
  itemList: ItemList = new ItemList();

  constructor(private api: ServerApiService, public themeService: ThemeService, private title: Title) {
    this.title.setTitle("Home - VideoTracker");
  }

  /**
   * Starting routine
   *
   * Gets the trending content to display on the home component.
   */
  ngOnInit(): void{
    // Get the trending content.
    this.api.getTrending(this.itemList);
  }

  /**
   * TODO: Loads the content of the item clicked on.
   * @param event
   */
  goto(event: any){
    var target = event.target;
    var value = target.attributes.id.nodeValue;
    var type = target.attributes.title.nodeValue;
    console.log(value);
    console.log(type);
  }
}
