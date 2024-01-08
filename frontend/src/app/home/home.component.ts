import { Component, OnInit } from '@angular/core';
import { Item, ItemList, KitsuItemAssigner, TMDBItemAssigner } from '../item';
import { ServerApiService } from '../server-api.service';
import { CommonModule } from '@angular/common';
import { strings } from '../strings';


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
    // First, get the server info to determine the API.
    this.api.getServerInfoObservable().subscribe({
      next: data => {
        switch(data.API){// Based on the API, get the trending content.
          case strings.KITSU: // Kitsu gets trending anime.
            this.api.getTrending(strings.anime, this.itemList, new KitsuItemAssigner());
            this.itemList.setTitle(strings.trending + " " + strings.anime);
            break;
          case strings.TMDB: // TMDB gets trending movies and tv.
            this.api.getTrending(strings.movie, this.itemList, new TMDBItemAssigner());
            this.api.getTrending(strings.tv, this.itemList, new TMDBItemAssigner());
            this.itemList.setTitle(strings.trending + " " + strings.movie + " and " + strings.tv);
            break;
          default:
            this.itemList.setTitle(strings.TRENDING_ERROR);
            break;
        }
      },
      error: error => {
        console.log(error);
        this.itemList.setTitle(strings.TRENDING_ERROR);
      },
    });
    
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
