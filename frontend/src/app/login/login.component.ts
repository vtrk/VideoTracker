import { Component } from '@angular/core';
import {FormsModule} from "@angular/forms";
import {AuthenticationService} from "../authentication.service";
import {ThemeService} from "../theme.service";
import {NgClass} from "@angular/common";
import {Router, RouterLink} from "@angular/router";
import {faHouse} from "@fortawesome/free-solid-svg-icons";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {ServerApiService} from "../services/server-api.service";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    NgClass,
    RouterLink,
    FaIconComponent
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  email_username: string = '';
  password: string = '';
  wrong_credentials: boolean = true;
  protected readonly faHouse = faHouse;


  constructor(private api: ServerApiService,private router: Router, private authService: AuthenticationService, public themeService: ThemeService) {}

  verify(){
    console.log(this.email_username, this.password);
    if(this.authService.login(this.email_username, this.password)){
      console.log('Login successful'+this.api.getUserId(this.email_username, this.password));
      this.router.navigate(['/home']);
    }else{
      this.wrong_credentials = false;
    }
  }

  show_message(){
    return this.wrong_credentials;
  }
}
