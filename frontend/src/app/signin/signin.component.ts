import { Component } from '@angular/core';
import { FormControl, FormGroup, FormsModule, NgForm, ReactiveFormsModule, Validators} from "@angular/forms";
import {ThemeService} from "../services/theme/theme.service";
import {CommonModule, NgClass} from "@angular/common";
import {AuthenticationService} from "../services/authentication/authentication.service";
import {faGear} from "@fortawesome/free-solid-svg-icons";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {Router, RouterLink} from "@angular/router";
import {CookieService} from "ngx-cookie-service";
import { strings } from '../utils/strings';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-signin',
  standalone: true,
  imports: [
    FormsModule,
    NgClass,
    RouterLink,
    FaIconComponent,
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './signin.component.html',
  styleUrl: './signin.component.css'
})
export class SigninComponent {
  form = new FormGroup({ 
    "email": new FormControl("", Validators.required), 
    "username": new FormControl("", Validators.required), 
    "password": new FormControl("", Validators.required), 
    "confirmPassword": new FormControl("", Validators.required)});

  protected readonly faGear = faGear;

  email_in_use_error: string = strings.email_in_use;
  email_in_use: boolean = false;

  password_mismatch_error: string = strings.password_mismatch;
  password_mismatch: boolean = false;

  registration_error: string = strings.registration_error;
  registration: boolean = false;

  constructor(private router: Router, public themeService: ThemeService, private authService: AuthenticationService, private cookieService: CookieService, private title: Title) {
    this.title.setTitle("Sign In");
  }
  signIn(form: NgForm){
    this.email_in_use = false;
    this.password_mismatch = false;
    this.registration = false;

    let email = form.form.controls['email'].value;
    let username = form.form.controls['username'].value;
    let password = form.form.controls['password'].value;
    let confirmPassword = form.form.controls['confirmPassword'].value;
    if(email == null || username == null || password == null || confirmPassword == null){
      this.registration = true;
      return;
    }

    if(password != confirmPassword){
      this.password_mismatch = true;
      return;
    }

    this.authService.signIn(email, username, password).subscribe({
      next: data => {
        let json = JSON.parse(JSON.stringify(data));
        switch(json.response){
          case "email_in_use":
            this.email_in_use = true;
            break;
          case "registration_failed":
            this.registration = true;
            break;
          default:
            this.cookieService.set('id_user', json.response);
            this.router.navigate(['/home']);
            break;
        }
      },
      error: error => {
        console.error(error);
        this.registration = true;
      }
    });
  }
}