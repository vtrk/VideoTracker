import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { SearchBody, ServerAPIResponse } from '../../utils/data-structures';
import { ItemAssigner, ItemList, KitsuItemAssigner, TMDBItemAssigner } from '../../utils/item';
import { strings } from '../../strings';
import { KitsuContent, TMDBContent } from '../../utils/content';
import { CookieService } from 'ngx-cookie-service';
import { ItemUserList } from '../../utils/item-user-list';
import { UserData } from '../../utils/user-data';
import { Observable } from 'rxjs';


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

  constructor(private client: HttpClient, private cookieService: CookieService) {
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
            item.setValues(json.id, json.title, json.overview, strings.TMDB_poster_url + json.poster_path, json.release_date, strings.movie, json.runtime);
            break;
          case strings.tv:
            item.setValues(json.id, json.name, json.overview, strings.TMDB_poster_url + json.poster_path, json.first_air_date, strings.tv ,undefined, json.number_of_seasons, json.number_of_episodes);
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

  getDbList(userList: ItemUserList, id_user: string){
    let url = environment.API_URL + '/list';
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    let JSONBody = {
      id_user: id_user
    };
    this.client.post(url, JSONBody, options).subscribe({
      next: data => {
        let json = JSON.parse(JSON.stringify(data));
        console.log(json);
        json.content.forEach((element: any) => {
          userList.addToList({
            id: element.id,
            poster: element.poster,
            title: element.title,
            status: element.state,
            type: element.type
          });
        });
        return;
      },
      error: error => {
        console.log(error);
        userList.setErrorMessage(strings.LIST_ERROR);
        return;
      }
    });
  }

  getDbNotifications(itemList: ItemList){
    let url = environment.API_URL + '/notifications';
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    let JSONBody = {
      id_user: this.cookieService.get('id_user')
    };
    this.client.post(url, JSONBody, options).subscribe({
      next: data => {
        let json = JSON.parse(JSON.stringify(data));
        console.log(json);
        return;
      },
      error: error => {
        console.log(error);
        itemList.setTitle(strings.CONTENT_ERROR);
        return;
      }
    });
  }

  login(email_username: string,  password: string): Observable<string>{
    let url = environment.API_URL + '/login';
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    let JSONBody = {
      email_username: email_username,
      password: password
    };
    return this.client.post<string>(url, JSONBody, options);
  }

  /**
   * Calls the server endpoint to register a new user.
   * @param email
   * @param username
   * @param password
   * @returns observable of the response from the server
   */
  signIn( email: string, username : string, password: string): Observable<string> {
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
    return this.client.post<string>(url, JSONBody, options);
  }

  changeEmail(id_user : string, email: string):string{
    let url = environment.API_URL + '/update';
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    let JSONBody = {
      id_user: id_user,
      credential: email,
      choice: 1
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
    return '0';
  }

  changePassword(id_user : string, password: string):string{
    let url = environment.API_URL + '/update';
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    let JSONBody = {
      id_user: id_user,
      credential: password,
      choice: 2
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
    return '0';
  }

  changeUsername(id_user: string,username : string):string{
    let url = environment.API_URL + '/update';
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    let JSONBody = {
      id_user: id_user,
      credential: username,
      choice: 3
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
    return '0';
  }

  getInfoProfile(user: UserData){
    let url = environment.API_URL + '/profile';
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    let JSONBody = {
      id_user: this.cookieService.get('id_user')
    };
    this.client.post(url, JSONBody, options).subscribe({
      next: data => {
        let json = JSON.parse(JSON.stringify(data));
        user.setValues(json.id, json.username, json.email, json.watching, json.completed, json.on_hold, json.dropped, json.plan_to_watch);
        return;
      },
      error: error => {
        user.setErrorMessage(strings.PROFILE_ERROR);
        return;
      }
    });
  }


  addToList(id_content : string, status : string, title : string, duration: string, n_episode: string, link: string, type: string){
    let url = environment.API_URL + '/addToList';
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    let JSONBody = {
      id_user: this.cookieService.get('id_user'),
      id_content: id_content + '_' + type,
      status: status,
      title: title,
      duration: duration,
      n_episode: n_episode == undefined ? '-1' : n_episode,
      link: link
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
  }

  removeFromList(id_content : string, type: string){
    let url = environment.API_URL + '/removeFromList';
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    let JSONBody = {
      id_user: this.cookieService.get('id_user'),
      id_content: id_content + '_' + type,
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
    //first, need to find the id_list by id_user, then you can add using containsDaoPostgres add
  }

  getReview(id_content : string, type: string){
    let url = environment.API_URL + '/getReview';
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    let JSONBody = {
      id_user: this.cookieService.get('id_user'),
      id_content: id_content + '_' + type
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
  }

  addReview(id_content : string, type: string, vote: string, review: string){
    let url = environment.API_URL + '/addReview';
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    let JSONBody = {
      id_user: this.cookieService.get('id_user'),
      id_content: id_content + '_' + type,
      vote: vote,
      review: review
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
  }

}