import { Component } from '@angular/core';
import {NgClass} from "@angular/common";
import {ThemeService} from "../theme.service";

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [
    NgClass
  ],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.css'
})
export class UserListComponent {
  constructor(public themeService: ThemeService) {
  }
}
