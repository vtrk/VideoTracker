import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from "../services/authentication-service/authentication.service";
import { FormsModule, NgForm } from "@angular/forms";
import { ThemeService } from "../theme.service";
import { CommonModule, NgClass } from "@angular/common";
import { CookieService } from "ngx-cookie-service";
import { ServerApiService } from "../services/server-api-service/server-api.service";
import { Router } from "@angular/router";
import { strings } from '../utils/strings';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faUserMinus } from '@fortawesome/free-solid-svg-icons';
import { Title } from "@angular/platform-browser";

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [
    FormsModule,
    NgClass,
    CommonModule,
    FontAwesomeModule,
  ],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.css'
})
export class SettingsComponent implements OnInit{
  server_error: boolean = false;
  sever_error_message: string = strings.SERVER_ERROR;

  wrong_password_deletion: boolean = false;
  wrong_password_deletion_message: string = strings.WRONG_PASSWORD;

  error_change_username: boolean = false;
  error_change_username_message: string = strings.ERROR_CHANGE_USERNAME;

  emails_dont_match: boolean = false;
  emails_dont_match_message: string = strings.EMAILS_DONT_MATCH;
  error_change_email: boolean = false;
  error_change_email_message: string = strings.ERRIR_CHANGE_EMAIL;

  password_mismatch: boolean = false;
  password_mismatch_message: string = strings.password_mismatch;
  wrong_password_error: boolean = false;
  password_error_message: string = strings.wrong_credentials;

  faUserMinus = faUserMinus;

  constructor(private router: Router,private api: ServerApiService,private cookieService: CookieService,private authService: AuthenticationService, public themeService : ThemeService, private title: Title) {
    this.title.setTitle("Settings");
  }

  /**
   * Send a request to the server to change the password
   * @param form 
   * @returns 
   */
  change_password(form: NgForm){
    this.password_mismatch = false;
    this.wrong_password_error = false;

    if((form.value.new_password == '' || form.value.confirm_password == '') || (form.value.new_password != form.value.confirm_password)){
      this.password_mismatch = true;
      return;
    }
    this.api.changePassword(this.cookieService.get('id_user'), form.value.new_password).subscribe({
      next: (data) => {
        this.authService.logout();
        this.router.navigate(['/home']);
      },
      error: (err) => {
        console.log(err);
        this.wrong_password_error = true;
      },
    });
  }

  /**
   * Send a request to the server to change the email
   * @param form 
   * @returns 
   */
  change_email(form: NgForm){
    this.emails_dont_match = false;
    this.error_change_email = false;

    if((form.value.new_email == '' || form.value.confirm_email == '') || (form.value.new_email != form.value.confirm_email)){
      this.emails_dont_match = true;
      return;
    }

    this.api.changeEmail(this.cookieService.get('id_user'), form.value.new_email).subscribe({
      next: (data) => {
        this.router.navigate(['/profile']);
      },
      error: (err) => {
        this.error_change_email = true;
        console.log(err);
      },
    });

  }

  /**
   * Send a request to the server to change the username
   * @param form 
   * @returns 
   */
  change_username(form: NgForm){
    this.error_change_username = false;
    if(form.value.username == ''){
      this.error_change_username = true;
      return;
    }
    this.api.changeUsername(this.cookieService.get('id_user'), form.value.new_username).subscribe({
      next: (data) => {
        this.router.navigate(['/profile']);
      },
      error: (err) => {
        this.error_change_username = true;
        console.log(err);
      },
    });
  }

  /**
   * Send a request to the server to delete the account
   * @param form 
   * @returns 
   */
  delete_account(form: NgForm){
    this.wrong_password_deletion = false;
    if(form.value.password == ''){
      this.wrong_password_deletion = true;
      return;
    }
    this.api.deleteAccount(form.value.password).subscribe({
      next: (data) => {
        let json = JSON.parse(JSON.stringify(data));
        if(json.response == '1')
          this.wrong_password_deletion = true;
        else{
          this.authService.logout();
          this.router.navigate(['/home']);
        }
      },
      error: (err) => {
        this.wrong_password_deletion = true;
        console.log(err);
      },
    });
  }

  ngOnInit(): void {
    
    // Check if server is responding, if not, display error message
    this.api.getServerInfoObservable().subscribe(
      {
        error: (err) => {
          this.server_error = true;
          console.log(err);
        },
      }
    );
  }

}
