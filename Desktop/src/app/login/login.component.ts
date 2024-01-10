import { Component } from '@angular/core';
import {FormsModule} from "@angular/forms";
import {AuthenticationService} from "../authentication.service";
import {ThemeService} from "../theme.service";
import {NgClass} from "@angular/common";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    NgClass
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  email: string = '';
  password: string = '';

  constructor(private authService: AuthenticationService, public themeService: ThemeService) {}

  verify(){
    console.log(this.email, this.password);
    console.log(this.authService.login(this.email, this.password));
  }

}
