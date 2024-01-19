import { Router } from '@angular/router';
import { Injectable } from '@angular/core';
import { AuthenticationService } from '../services/authentication-service/authentication.service';


@Injectable({
  providedIn: 'root'
})

export class AuthGuard {
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
