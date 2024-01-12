import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { SearchBody, ServerAPIResponse } from '../utils/data-structures';
import { ItemAssigner, ItemList, KitsuItemAssigner, TMDBItemAssigner } from '../utils/item';
import { strings } from '../strings';


/**
 * Service for interacting with the server API.
*/
@Injectable({
  providedIn: 'root'
})
export class ServerApiService {
  assigner: ItemAssigner;
  searchArgs: Map<string, string> = new Map<string, string>();
  searchType: string = '';

  constructor(private client: HttpClient) {
    // Get the server info to initialize the ItemAssigner.
    this.getServerInfoObservable().subscribe({
      next: data => {
        switch(data.API){
          case strings.KITSU:
            this.assigner = new KitsuItemAssigner();
            break;
          case strings.TMDB:
            this.assigner = new TMDBItemAssigner();
            break;
          default:
            break;
        }
      },
      error: error => {
        console.log(error);
      },
    });
  }

  /**
   * Sends and fetches data from a search query to the server.
   * @param searchBody JSON body of the search request
   * @param itemList list to add items to
   * 
   * This function has a wrapper @see {@link getSearch}
   */
  private fetchSearchQuery(body: SearchBody, itemList: ItemList) {
    let url = environment.API_URL + '/search';
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    this.client.post(url, body.getJSONBody(), options).subscribe({
      next: data => {
        let json = JSON.parse(JSON.stringify(data));
        this.assigner.assign(itemList, json);
        return;
      },
      error: error => {
        console.log(error);
        itemList.setTitle(strings.SEARCH_ERROR);
        return;
      }
    });
  }

  /**
   * Gets the results of a search query from the server and saves them to the itemList.
   * @param itemList list to add items to
   * @param searchBody JSON body of the search request
   */
  getSearch(itemList: ItemList, searchBody: SearchBody){
    // First, determine the API.
    this.getServerInfoObservable().subscribe({
      next: data => {
        switch(data.API){// Based on the API, get the search content.
          case strings.KITSU:// Kitsu searches anime.
            this.fetchSearchQuery(searchBody, itemList);
            itemList.setTitle(strings.trending + " " + strings.anime);
            break;
          case strings.TMDB:// TMDB searches either movies or tv.
            this.fetchSearchQuery(searchBody, itemList);
            itemList.setTitle(strings.trending + " " + strings.movie + " and " + strings.tv);
            break;
          default:
            itemList.setTitle(strings.TRENDING_ERROR);
            break;
        }
      },
      error: error => {
        console.log(error);
        itemList.setTitle(strings.TRENDING_ERROR);
      }
    });
  }

  /**
   * Fetches the trending items from the server.
   * @param type content type
   * @param itemList list to add items to
   * @param trendingString string to set to trending
   * 
   * This function has a wrapper @see {@link getTrending}
   */
  private fetchTrending(type: string, itemList: ItemList){
    let url = environment.API_URL + '/trending?type=' + type;
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    this.client.get(url, options).subscribe({
      next: data => {
        let json = JSON.parse(JSON.stringify(data));
        this.assigner.assign(itemList, json);
        return;
      },
      error: error => {
        console.log(error);
        itemList.setTitle(strings.TRENDING_ERROR);
        return;
      }
    })
  }

  /**
   * Gets the trending items from the server and saves them to the itemList.
   * @param itemList list to add items to
   */
  getTrending(itemList: ItemList){
    // First, determine the API.
    this.getServerInfoObservable().subscribe({
      next: data => {
        switch(data.API){// Based on the API, get the trending content.
          case strings.KITSU:// Kitsu gets trending anime.
            this.fetchTrending(strings.anime, itemList);
            itemList.setTitle(strings.trending + " " + strings.anime);
            break;
          case strings.TMDB:// TMDB gets trending movies and tv.
            this.fetchTrending(strings.movie, itemList);
            this.fetchTrending(strings.tv, itemList);
            itemList.setTitle(strings.trending + " " + strings.movie + " and " + strings.tv);
            break;
          default:
            itemList.setTitle(strings.TRENDING_ERROR);
            break;
        }
      },
      error: error => {
        console.log(error);
        itemList.setTitle(strings.TRENDING_ERROR);
      }
    });
  }

  /**
   * Gets the server info.
   * @returns An observable of the response from the server.
   */
  getServerInfoObservable(){
    return this.client.get<ServerAPIResponse>(environment.API_URL + '/api');
  }

  /**
   * Gets the server info.
   * @param info map to add info to
   */
  getServerInfo(info: Map<string, string>){
    this.client.get<ServerAPIResponse>(environment.API_URL + '/api').subscribe({
      next: data => {
        info.set("API", data.API);
        info.set("Version", data.Version);
        return;
      },
      error: error => {
        console.log(error);
        info.set("API", strings.API_ERROR);
        info.set("Version", strings.API_ERROR);
        return;
      }
    });
  }
}