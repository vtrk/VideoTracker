import { Component } from '@angular/core';
import {FormControl, FormGroup, FormsModule, NgForm, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthenticationService} from "../services/authentication-service/authentication.service";
import {ThemeService} from "../theme.service";
import {CommonModule, NgClass} from "@angular/common";
import {Router, RouterLink} from "@angular/router";
import {faHouse} from "@fortawesome/free-solid-svg-icons";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {ServerApiService} from "../services/server-api-service/server-api.service";
import { strings } from '../strings';
import { CookieService } from 'ngx-cookie-service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    NgClass,
    RouterLink,
    FaIconComponent,
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  wrong_credentials: boolean = false;
  wrong_credentials_str: string = strings.wrong_credentials;

  login_error: boolean = false;
  login_error_str: string = strings.login_error;

  protected readonly faHouse = faHouse;

  form = new FormGroup({
    "email": new FormControl("", Validators.required),
    "password": new FormControl("", Validators.required)
  });


  constructor(private api: ServerApiService,private router: Router, private authService: AuthenticationService, public themeService: ThemeService, private cookieService: CookieService) {}

  logIn(form: NgForm){
    this.wrong_credentials = false;
    this.login_error = false;

    let email = form.form.controls['email'].value;
    let password = form.form.controls['password'].value;

    if(email == null || password == null){
      this.login_error = true;
      return;
    }

    this.api.login(email, password).subscribe({
      next: data => {
        let json = JSON.parse(JSON.stringify(data));
        if(json.response == "0"){
          this.wrong_credentials = true;
        }else{
          this.cookieService.set('id_user', json.response);
          this.router.navigate(['/home']);
        }
      },
      error: error => {
        console.error(error);
        this.login_error = true;
      }
    });
  }

  /*
  verify(){
    console.log(this.email_username, this.password);
    if(this.authService.login(this.email_username, this.password)){
      console.log('Login successful');
      this.router.navigate(['/home']);
    }else{
      this.wrong_credentials = false;
    }
  }*/

  show_message(){
    return this.wrong_credentials;
  }
}
