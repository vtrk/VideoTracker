import { Component } from '@angular/core';
import {NgClass} from "@angular/common";
import {ThemeService} from "../theme.service";

@Component({
  selector: 'app-mailbox',
  standalone: true,
  imports: [
    NgClass
  ],
  templateUrl: './mailbox.component.html',
  styleUrl: './mailbox.component.css'
})
export class MailboxComponent {
  constructor(public themeService: ThemeService) {
  }
}
