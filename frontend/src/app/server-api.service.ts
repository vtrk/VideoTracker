import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { SearchBody } from './data-structures';


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

  getTrending(type: string){
    let url = environment.API_URL + '/trending?type=' + type;
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    return this.client.get(url, options);
  }

}
