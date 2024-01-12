import { Component } from '@angular/core';
import {FormsModule} from "@angular/forms";
import {ThemeService} from "../theme.service";
import {NgClass} from "@angular/common";
import {AuthenticationService} from "../authentication.service";
import {faGear} from "@fortawesome/free-solid-svg-icons";
import {FaIconComponent} from "@fortawesome/angular-fontawesome";
import {Router, RouterLink} from "@angular/router";

@Component({
  selector: 'app-signin',
  standalone: true,
  imports: [
    FormsModule,
    NgClass,
    RouterLink,
    FaIconComponent
  ],
  templateUrl: './signin.component.html',
  styleUrl: './signin.component.css'
})
export class SigninComponent {
  email: string = '';
  username: string = '';
  password: string = '';
  confirmPassword: string = '';

  constructor(private router: Router, public themeService: ThemeService, private authService: AuthenticationService){
  }
  signIn(){
    console.log(this.email, this.password, this.confirmPassword);
    console.log(this.authService.signIn(this.email,this.username, this.password, this.confirmPassword));
    this.router.navigate(['/home']);
  }

  protected readonly faGear = faGear;
}
