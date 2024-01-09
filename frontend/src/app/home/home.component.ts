import { Component, OnInit } from '@angular/core';
import { ItemList, } from '../utils/item';
import { ServerApiService } from '../services/server-api.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit{
  itemList: ItemList = new ItemList();

  constructor(private api: ServerApiService) {}

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
