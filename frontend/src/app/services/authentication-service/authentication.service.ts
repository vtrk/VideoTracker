import { Injectable } from '@angular/core';
import {CookieService} from "ngx-cookie-service";
import {ServerApiService} from "../server-api-service/server-api.service";
import { strings } from '../../strings';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private userIsAuthenticated = false;
  private check: string;
  constructor(private api: ServerApiService, private cookieService: CookieService) {}
  login(email: string, password: string):boolean{
    //Here goes the code to check if the email or username and password are correct
    this.api.login(email, password);

    if(this.check != null || this.check != "0"){
      this.userIsAuthenticated = true;
      console.log('login success');
      this.cookieService.set('id_user', this.check);
      return true;
    }
    console.log('login failed');
    return false;
  }

  signIn(email: string, username: string, password: string): Observable<String>{
    return this.api.signIn(email,username,password);
  }

  logout(){
    this.userIsAuthenticated = false;
    this.cookieService.set('id_user', "0");
  }

  get userIsAuth(){
    if(this.cookieService.get('id_user') != "0"){
      this.userIsAuthenticated = true;
    }
    return this.userIsAuthenticated;
  }

}
