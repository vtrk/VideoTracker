import { Component } from '@angular/core';
import {RouterLink} from "@angular/router";
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faGithub } from '@fortawesome/free-brands-svg-icons';

@Component({
  selector: 'app-footer',
  standalone: true,
    imports: [
        RouterLink,
        FontAwesomeModule
    ],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.css'
})
export class FooterComponent {
  faGithub = faGithub;
}
