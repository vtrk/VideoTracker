import { Injectable } from '@angular/core';
import {CookieService} from "ngx-cookie-service";
import {ServerApiService} from "./services/server-api.service";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private userIsAuthenticated = false;
  private check: string;
  constructor(private api: ServerApiService, private cookieService: CookieService) {}
  login(email: string, password: string):boolean{
    //Here goes the code to check if the email or username and password are correct
    this.check = this.api.getUserId(email, password);
    if(this.check != null || this.check != "0"){
      this.userIsAuthenticated = true;
      console.log('login success');
      this.cookieService.set('IdUser', this.check);
      return true;
    }
    console.log('login failed');
    return false;
  }

  signIn(email: string,username: string, password: string, confirmPassword: string):boolean{
    //Check if the email is already in use
    this.check = this.api.getUserId(email, password);
    if(this.check != null || this.check != "0") {
      console.log('Sign in failed');
      return false;
    }
    //check if the password and the confirm password are the same
    if(password != confirmPassword){
      console.log('Sign in failed');
      return false;
    }
    console.log('Sign in success');
    this.userIsAuthenticated = true;
    this.check = this.api.getSignIn(email,username,password);
    this.cookieService.set('IdUser', this.check);
    return true;
  }

  logout(){
    this.userIsAuthenticated = false;
    this.cookieService.set('IdUser', "0");
  }

  get userIsAuth(){
    if(this.cookieService.get('IdUser') != "0"){
      this.userIsAuthenticated = true;
    }
    return this.userIsAuthenticated;
  }

}
