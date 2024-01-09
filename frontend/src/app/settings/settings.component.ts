import { Component } from '@angular/core';
import {AuthenticationService} from "../authentication.service";
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.css'
})
export class SettingsComponent {
  old_email: string = '';
  new_email: string = '';
  change_ema: string = '';
  old_password: string = '';
  new_password: string = '';
  change_pass: string = '';
  old_username: string = '';
  new_username: string = '';
  change_user: string = '';
  dark_theme: boolean = false;

  constructor(private authService: AuthenticationService) {}

  change_password(){
    console.log(this.old_password, this.new_password, this.change_pass);
    //qui bisogna chiama il server per cambiare la password
  }

  change_email(){
    console.log(this.old_email, this.new_email, this.change_ema);
    //qui bisogna chiama il server per cambiare l'email
  }

  change_username(){
    console.log(this.old_username, this.new_username, this.change_user);
    //qui bisogna chiama il server per cambiare l'username
  }

  set_dark_theme(){
    console.log("Dark theme set");
  }

  set_light_theme(){
    console.log("Light theme set");
  }

}
