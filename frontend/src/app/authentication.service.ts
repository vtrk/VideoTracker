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

  logout(){
    this.userIsAuthenticated = false;
  }

  get userIsAuth(){
    return this.userIsAuthenticated;
  }

}
