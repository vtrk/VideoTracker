import { Router } from "@angular/router";
import { AuthenticationService } from "../services/authentication/authentication.service";
import { ServerApiService } from "../services/server-api/server-api.service";
import { CookieService } from "ngx-cookie-service";

/**
 * Checks if the user is banned, if so, it logs them out.
 * @param id_user The id of the user.
 * @param authService The authentication service.
 * @param router The router.
 * @param server The server api service.
 * @param cookieService The cookie service.
 */
export function checkBan(id_user: string, authService: AuthenticationService, router: Router, server: ServerApiService, cookieService: CookieService) {
  server.isBanned(id_user).subscribe({
    next: data => {
      let json = JSON.parse(JSON.stringify(data));
      if(json.response == '1'){
        authService.logout();
        router.navigate(['/home']);
      }
    },
    error: error => {
      console.log(error);
    }
  });
}