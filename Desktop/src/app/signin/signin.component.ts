import { Component } from '@angular/core';
import {FormsModule} from "@angular/forms";
import {ThemeService} from "../theme.service";
import {NgClass} from "@angular/common";

@Component({
  selector: 'app-signin',
  standalone: true,
  imports: [
    FormsModule,
    NgClass
  ],
  templateUrl: './signin.component.html',
  styleUrl: './signin.component.css'
})
export class SigninComponent {
  email: string = '';
  password: string = '';
  confirmPassword: string = '';

  constructor(public themeService: ThemeService){
  }
  signin(){
    console.log(this.email, this.password, this.confirmPassword);
  }
}
