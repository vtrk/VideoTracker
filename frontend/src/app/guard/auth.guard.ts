import { Router } from '@angular/router';
import { Injectable } from '@angular/core';
import { AuthenticationService } from '../services/authentication/authentication.service';
import { ServerApiService } from '../services/server-api/server-api.service';
import { CookieService } from 'ngx-cookie-service';
import { checkBan } from '../utils/check-ban';


@Injectable({
  providedIn: 'root'
})

export class AuthGuard {
  constructor(private authService: AuthenticationService, private router: Router, private server: ServerApiService, private cookieService: CookieService) {}

  canActivate(): boolean {
    checkBan(this.cookieService.get('id_user'), this.authService, this.router, this.server, this.cookieService);
    if(this.authService.userIsAuth){
      return true;
    }else{
      this.router.navigate(['/login']);
      return false;
    }
  }
}
