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
  error_email: boolean = true;
  new_email: string = '';
  confirm_email: string = '';
  error_password: boolean = true;
  new_password: string = '';
  confirm_password: string = '';
  new_username: string = '';

  constructor(private api: ServerApiService,private cookieService: CookieService,private authService: AuthenticationService, public themeService : ThemeService) {}

  change_password(){
    console.log(this.new_password, this.confirm_password);
    //qui bisogna chiama il server per cambiare la password
    if(this.new_password == this.confirm_password){
      this.api.changePassword(this.cookieService.get('id_user'),this.new_password);
    }else{
      this.error_password = true;
    }
  }

  change_email(){
    console.log(this.new_email, this.confirm_email);
    //qui bisogna chiama il server per cambiare l'email
    if(this.new_email == this.confirm_email){
      this.api.changeEmail(this.cookieService.get('id_user'),this.new_email);
    }else{
      this.error_email = true;
    }
  }

  change_username(){
    console.log(this.new_username);
    //qui bisogna chiama il server per cambiare l'username
    this.api.changeUsername(this.cookieService.get('id_user'),this.new_username);
  }

  show_message_password(){
    return this.error_password;
  }
  show_message_email(){
    return this.error_email;
  }

}
