import {CanActivate, Router, RouterModule} from '@angular/router';
import { Injectable } from '@angular/core';
import { AuthenticationService } from './authentication.service';


@Injectable({
  providedIn: 'root'
})

export class AuthGuard implements CanActivate {
  constructor(private authService: AuthenticationService, private router: Router) {}

  canActivate(): boolean {
    console.log(this.authService.userIsAuth);
    if(this.authService.userIsAuth){
      return true;
    }else{
      this.router.navigate(['/login']);
      return false;
    }
  }
}
