import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { SearchBody, ServerAPIResponse } from '../../utils/data-structures';
import { ItemAssigner, ItemList, KitsuItemAssigner, TMDBItemAssigner } from '../../utils/item';
import { strings } from '../../utils/strings';
import { KitsuContent, TMDBContent } from '../../utils/content';
import { CookieService } from 'ngx-cookie-service';
import { ItemUserList } from '../../utils/item-user-list';
import { UserData } from '../../utils/user-data';
import { Observable } from 'rxjs';
import { ItemMailList } from '../../utils/mail-list';
import { ReviewList } from '../../utils/reviews';

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

  /**
   * Gets the content of an item from the server (TMDB API).
   * @param item Content object to add data to
   * @param type type of content
   * @param id id of content
   */
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
   * @param body JSON body of the search request
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

  /**
   * Gets the list of items of a user from the server.
   * @param userList 
   * @param id_user 
   */
  getList(userList: ItemUserList, id_user: string){
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

  /**
   * Gets the notifications of a user from the server.
   * @param itemMailList 
   */
  getNotifications(itemMailList: ItemMailList){
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
        json.notifications.forEach((element: any) => {
          itemMailList.add(element.id, element.description, element.title);
        });
        return;
      },
      error: error => {
        console.log(error);
        itemMailList.setErrorString(strings.NOTIFICATIONS_ERROR);
        return;
      }
    });
  }

  /**
   * Calls the server endpoint to add a notification to a user.
   * @param id_user 
   * @param id_notification 
   * @returns an observable of the response from the server
   */
  removeNotification(id_user: string, id_notification: string): Observable<String>{
    let url = environment.API_URL + '/removeNotification';
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    let JSONBody = {
      id_user: id_user,
      id_notification: id_notification
    };
    return this.client.post<String>(url, JSONBody, options);
  }

  /**
   * Calls the server endpoint to login a user.
   * @param email_username 
   * @param password 
   * @returns an observable of the response from the server
   */
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

  /**
   * Calls the server endpoint to change the email of a user.
   * @param id_user
   * @param email
   * @returns an observable of the response from the server
   */
  changeEmail(id_user : string, email: string): Observable<String>{
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
    return this.client.post<String>(url, JSONBody, options);
  }

  /**
   * Calls the server endpoint to change the password of a user.
   * @param id_user
   * @param password
   * @returns an observable of the response from the server
   */
  changePassword(id_user : string, password: string): Observable<String>{
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
    return this.client.post<String>(url, JSONBody, options);
  }

  /**
   * Calls the server endpoint to change the username of a user.
   * @param id_user
   * @param username
   * @returns an observable of the response from the server
   */
  changeUsername(id_user: string, username : string): Observable<string>{
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
    return this.client.post<string>(url, JSONBody, options);
  }

  /**
   * Gets the profile info of a user from the server.
   * @param user data structure to add data to
   */
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

  /**
   * Adds an item to the user's list.
   * @param id_content
   * @param status
   * @param title
   * @param duration
   * @param n_episode
   * @param link
   * @param type
   */
  addToList(id_content : string, status : string, title : string, duration: string, n_episode: string, link: string, type: string){
    let url = environment.API_URL + '/addToList';
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    if(duration == undefined) duration = '-1'; // If the duration is undefined, set it to -1. (happens with running tv shows)
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

  /**
   * Removes an item from the user's list.
   * @param id_user
   * @param id_content
   * @returns an observable of the response from the server
   */
  removeFromList(id_user : string, id_content: string): Observable<String>{
    let url = environment.API_URL + '/removeFromList';
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    let JSONBody = {
      id_user: id_user,
      id_content: id_content
    };
    return this.client.post<String>(url, JSONBody, options);
  }

  /**
   * Gets the reviews of a content from the server.
   * @param id_content the id of the content
   * @param reviewList the list to add the reviews to
   */
  getReview(id_content : string, reviewList: ReviewList){
    let url = environment.API_URL + '/reviews';
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    let JSONBody = {
      id_content: id_content
    };
    this.client.post(url, JSONBody, options).subscribe({
      next: data => {
        let json = JSON.parse(JSON.stringify(data));
        json.reviews.forEach((element: any) => {
          reviewList.add(element.id, element.vote, element.user_comment, element.username, element.id_user, element.id_content);
        });
      },
      error: error => {
        console.log(error);
        reviewList.setErrorString(strings.REVIEW_ERROR);
      }
    });
  }

  /**
   * Removes a review from a content.
   * @param id_user
   * @param id_content
   * @param type
   * @returns an observable of the response from the server
   */
  removeReview(id_user : string, id_content: string, type: String): Observable<String>{
    let url = environment.API_URL + '/removeReview';
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    let JSONBody = {
      id_user: id_user,
      id_content: id_content + '_' + type
    };
    return this.client.post<String>(url, JSONBody, options);
  }

  /**
   * Adds a review to a content.
   * @param id_content 
   * @param type 
   * @param vote 
   * @param user_comment
   * <br>
   * Parameters used to add the content to the database if it doesn't exist
   * @param title 
   * @param duration 
   * @param n_episode 
   * @param link 
   */
  addReview(id_content : string, type: string, vote: string, user_comment: string, title: string, duration: string, n_episode: string, link: string): Observable<String>{
    let url = environment.API_URL + '/addReview';
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    if(duration == undefined) duration = '-1';
    if(n_episode == undefined) n_episode = '-1';
    let JSONBody = {
      vote: vote,
      user_comment: user_comment,
      id_user: this.cookieService.get('id_user'),
      id_content: id_content + '_' + type,
      title: title,
      duration: duration,
      n_episode: n_episode,
      link: link
    };
    return this.client.post<String>(url, JSONBody, options);
  }

  /**
   * Deletes the account of a user.
   * @param password 
   * @returns an observable of the response from the server
   */
  deleteAccount(password: string): Observable<String>{
    let url = environment.API_URL + '/removeUser';
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    let JSONBody = {
      id_user: this.cookieService.get('id_user'),
      password: password
    };
    return this.client.post<String>(url, JSONBody, options);
  }

  /**
   * Calls the server endpoint to check if the given user is banned.
   * @param id_user
   * @returns an observable of the response from the server
   */
  isBanned(id_user: string): Observable<String>{
    let url = environment.API_URL + '/isBanned';
    let options = {
      headers: {
        'Content-Type': 'text/plain',
        'Accept': 'text/plain'
      }
    };
    return this.client.post<String>(url, {id_user: id_user}, options);
  }
}
