import { Injectable } from '@angular/core';
import {CookieService} from "ngx-cookie-service";
import {ServerApiService} from "../server-api/server-api.service";
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private userIsAuthenticated = false;
  private check: string;
  constructor(private api: ServerApiService, private cookieService: CookieService) {}

  login(email: string, password: string): Observable<String>{
    return this.api.login(email,password);
  }

  signIn(email: string, username: string, password: string): Observable<String>{
    return this.api.signIn(email,username,password);
  }

  logout(){
    this.userIsAuthenticated = false;
    this.cookieService.set('id_user', "0");
  }

  get userIsAuth(){
    let cookie = this.cookieService.get('id_user');
    if(!cookie)
      return this.userIsAuthenticated;
    
    if(this.cookieService.get('id_user') != "0"){
      this.userIsAuthenticated = true;
    }
    return this.userIsAuthenticated;
  }

}
