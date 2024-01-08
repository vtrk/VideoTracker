import { Injectable } from '@angular/core';
import { Item } from './item';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environments/environment';
import { SearchBody } from './data-structures';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  searchResults: Item[] = [];
  response: string = '';

  constructor(private client: HttpClient) {}

  /**
   * Sends a search query to the server.
   * @param query 
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
    this.client.post(url, body.getJSONBody(), options).subscribe(
      (response: Object) => {
        this.response = JSON.stringify(response);
        console.log(this.response);
      },
      (error: any) => {
        console.log(error);
      }
    );
  }
}
