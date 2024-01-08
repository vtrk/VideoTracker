import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { SearchBody, ServerAPIResponse } from './data-structures';
import { ItemAssigner, ItemList } from './item';
import { strings } from './strings';


/**
 * Service for interacting with the server API.
*/

@Injectable({
  providedIn: 'root'
})
export class ServerApiService {

  constructor(private client: HttpClient) { }


  /**
   * Sends a search query to the server.
   * @param query
   * @returns An observable of the response from the server.
   */
  sendSearchQuery(query: string, type: string, args: Map<string, string>) {
    let url = environment.API_URL + '/search?query=' + query;
    let body = new SearchBody(type, query, args);
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    return this.client.post(url, body.getJSONBody(), options)
  }

  /**
   * Gets the trending items from the server.
   * @param type content type
   * @param itemList list to add items to
   * @param trendingString string to set to trending
   */
  getTrending(type: string, itemList: ItemList, assigner: ItemAssigner){
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
        json.data.forEach((element: any) => {
          assigner.assign(itemList, element);
        });
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
