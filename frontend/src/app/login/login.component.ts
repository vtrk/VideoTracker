import { Component } from '@angular/core';
import {FormControl, FormGroup, FormsModule, NgForm, ReactiveFormsModule, Validators} from "@angular/forms";
import {ThemeService} from "../services/theme/theme.service";
import {CommonModule, NgClass} from "@angular/common";
import {Router, RouterLink} from "@angular/router";
import {faHouse} from "@fortawesome/free-solid-svg-icons";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {ServerApiService} from "../services/server-api/server-api.service";
import { strings } from '../utils/strings';
import { CookieService } from 'ngx-cookie-service';
import { Title } from '@angular/platform-browser';

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

  ban_error: boolean = false;
  ban_banned_str: string = strings.BAN_ERROR;

  protected readonly faHouse = faHouse;

  form = new FormGroup({
    "email": new FormControl("", Validators.required),
    "password": new FormControl("", Validators.required)
  });


  constructor(private api: ServerApiService,private router: Router, public themeService: ThemeService, private cookieService: CookieService, private title: Title) {
    this.title.setTitle("Login");
  }

  logIn(form: NgForm){
    this.wrong_credentials = false;
    this.login_error = false;
    this.ban_error = false;

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
        }else if(json.response == "-1"){
          this.ban_error = true;
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

  show_message(){
    return this.wrong_credentials;
  }
}
