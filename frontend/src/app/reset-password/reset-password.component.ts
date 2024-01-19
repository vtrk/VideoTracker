import { Component } from '@angular/core';
import {FormsModule} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthenticationService} from "../services/authentication/authentication.service";
import {ThemeService} from "../services/theme/theme.service";
import {NgClass} from "@angular/common";

@Component({
  selector: 'app-reset-password',
  standalone: true,
    imports: [
        FormsModule,
        NgClass
    ],
  templateUrl: './reset-password.component.html',
  styleUrl: './reset-password.component.css'
})
export class ResetPasswordComponent {
  email: string = '';
  wrong_credentials: boolean = true;
  constructor(private router: Router, private authService: AuthenticationService, public themeService: ThemeService) {}

  recover(){
    console.log(this.email);
  }

  show_message(){
    return this.wrong_credentials;
  }

}
