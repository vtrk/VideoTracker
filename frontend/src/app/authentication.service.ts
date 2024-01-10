import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private userIsAuthenticated = false;
  constructor() {}
  login(email: string, password: string):boolean{
    //Qui bisogna mettere la chiamata al server per il login
    if(email == 'admin' && password == 'admin') {
      this.userIsAuthenticated = true;
      console.log('login success');
      return true;
    }
    console.log('login failed');
    return false;
  }

  signIn(email: string, password: string, confirmPassword: string):boolean{
    //Check if the email is already in use
    /*if() {
      console.log('Sign in failed');
      return false;
    }
    //check if the password and the confirm password are the same
    if(password != confirmPassword){
      console.log('Sign in failed');
      return false;
    }
    */
    console.log('Sign in success');
    this.userIsAuthenticated = true;
    return true;
  }

  logout(){
    this.userIsAuthenticated = false;
  }

  get userIsAuth(){
    return this.userIsAuthenticated;
  }

}
