import { Component } from '@angular/core';
import {AuthenticationService} from "../authentication.service";
import {FormsModule} from "@angular/forms";
import {ThemeService} from "../theme.service";
import {NgClass} from "@angular/common";
import {CookieService} from "ngx-cookie-service";
import {ServerApiService} from "../services/server-api.service";

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [
    FormsModule,
    NgClass
  ],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.css'
})
export class SettingsComponent {
  old_email: string = '';
  new_email: string = '';
  confirm_email: string = '';
  old_password: string = '';
  new_password: string = '';
  confirm_password: string = '';
  new_username: string = '';

  constructor(private api: ServerApiService,private cookieService: CookieService,private authService: AuthenticationService, public themeService : ThemeService) {}

  change_password(){
    console.log(this.old_password, this.new_password, this.confirm_password);
    //qui bisogna chiama il server per cambiare la password
  }

  change_email(){
    console.log(this.old_email, this.new_email, this.confirm_email);
    //qui bisogna chiama il server per cambiare l'email
  }

  change_username(){
    console.log(this.new_username);
    //qui bisogna chiama il server per cambiare l'username
  }

}
