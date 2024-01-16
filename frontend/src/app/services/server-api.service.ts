import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { SearchBody, ServerAPIResponse } from '../utils/data-structures';
import { ItemAssigner, ItemList, KitsuItemAssigner, TMDBItemAssigner } from '../utils/item';
import { strings } from '../strings';
import { Content, KitsuContent, TMDBContent } from '../utils/content';


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


  getKitsuContent(item: KitsuContent, type: string, id: string){
    // Since the type of content is known, the API is known so no need to request the API type from the server.
    let url = environment.API_URL + '/content/' + id + '?type=' + type;
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    this.client.get(url, options).subscribe({
      next: data => {
        let json = JSON.parse(JSON.stringify(data));
          console.log(json.data.titles)
          item.setValues(json.data.id, json.data.attributes.titles.en_jp, json.data.attributes.synopsis, json.data.attributes.posterImage.original, json.data.attributes.episodeCount, json.data.attributes.episodeLength, json.data.attributes.showType, json.data.attributes.status, json.data.attributes.startDate, json.data.attributes.endDate, json.data.attributes.ageRating, '');

        return;
      },
      error: error => {
        console.log(error);
        item.setErrorString(strings.CONTENT_ERROR);
        return;
      }
    })
  }

  /**
   * Gets the content of an item from the server (TMDB API).
   * @param item Content object to add data to
   * @param type type of content
   * @param id id of content
   */
  getTMDBContent(item: TMDBContent, type: string, id: string){
    // Since the type of content is known, the API is known so no need to request the API type from the server.
    let url = environment.API_URL + '/content/' + id + '?type=' + type;
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    this.client.get(url, options).subscribe({
      next: data => {
        let json = JSON.parse(JSON.stringify(data));
        switch(type){
          case strings.movie:
            item.setValues(json.id, json.title, json.overview, strings.TMDB_poster_url + json.poster_path, json.release_date, json.runtime);
            break;
          case strings.tv:
            item.setValues(json.id, json.name, json.overview, strings.TMDB_poster_url + json.poster_path, json.first_air_date, undefined, json.number_of_seasons, json.number_of_episodes);
            break;
          default:
            item.setErrorString(strings.CONTENT_ERROR);
            break;
        }
        return;
      },
      error: error => {
        console.log(error);
        item.setErrorString(strings.CONTENT_ERROR);
        return;
      }
    })
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
            break;
          case strings.TMDB:// TMDB searches either movies or tv.
            this.fetchSearchQuery(searchBody, itemList);
            break;
          default:
            itemList.setTitle(strings.TRENDING_ERROR);
            break;
        }
        itemList.setTitle(strings.SEARCH_RESULTS);
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

  getDbList(itemList: ItemList){
    let url = environment.API_URL + '/dbUserlist';
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    return this.client.get(url, options);
    /*
    Frontend:
    1. add cookieService to the constructor.
    2. this.cookieService.get('id_user'); // returns the id of the user as a string.
    Backend:
    1. Get the list id using id_user.
    2. UserList list = UserListDaoPostgres.findByIdUser((int)id_user);
    3. Get the list of the user using ContainsDaoPostgres.findContentInList(list.getId), it returns a list of content.
    */
  }

  getDbNotifications(itemList: ItemList){
    let url = environment.API_URL + '/dbNotifications';
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    return this.client.get(url, options);
    /*
    Frontend
    1. add cookieService to the constructor.
    2. this.cookieService.get('id_user'); // returns the id of the user as a string.
    Backend:
    1. Get the notifications using ReceiveDaoPostgres.findByIdUser((int) id_user), it returns a list of notifications.
    */
  }

  login( email_username: string,  password: string):string{
    let url = environment.API_URL + '/dbUserid';
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    return "1";
    /*
    Use this function in the backend:
    User find = UserDaoPostgres.findByEmailOrUsername(email_username, password);
    if(find.getIdUser() != 0){
      return find.getIdUser().toString(); //This is the result that I need
    }
    return "0";
    */
  }

  signIn( email: string, username : string, password: string): string {
    let url = environment.API_URL + '/register';
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    let JSONBody = {
      email: email,
      username: username,
      password: password
    };
    this.client.post(url, JSONBody, options).subscribe({
      next: data => {
        let json = JSON.parse(JSON.stringify(data));
        console.log(data);
        return data.toString();
      },
      error: error => {
        console.log(error);
        return error.toString();
      }
    });
    return '';
  }

  changeEmail(id_user : string, email: string):string{
    let url = environment.API_URL + '/dbChangeEmail';
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    return "1";
    /*
    Use this function in the backend:
    UserDaoPostgres.updateFromSettings((int)id_user, email, 1);
    */
  }

  changePassword(id_user : string, password: string):string{
    let url = environment.API_URL + '/dbChangePassword';
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    return "1";
    /*
    Use this function in the backend:
    UserDaoPostgres.updateFromSettings((int)id_user, password, 2);
    */
  }

  changeUsername(id_user: string,username : string):string{
    let url = environment.API_URL + '/dbChangeUsername';
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    return "1";
    /*
    Use this function in the backend:
    UserDaoPostgres.updateFromSettings((int)id_user, username, 3);
    */
  }

}
