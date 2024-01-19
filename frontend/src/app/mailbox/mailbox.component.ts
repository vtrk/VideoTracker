import { Component, OnInit } from '@angular/core';
import { NgClass } from "@angular/common";
import { ThemeService } from "../services/theme/theme.service";
import { CommonModule } from '@angular/common';
import { ServerApiService } from "../services/server-api-service/server-api.service";
import { ItemMailList } from '../utils/mail-list';
import { faTrashCan } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { strings } from '../utils/strings';
import { CookieService } from 'ngx-cookie-service';
import { Title } from '@angular/platform-browser';
@Component({
  selector: 'app-mailbox',
  standalone: true,
  imports: [
    CommonModule,
    NgClass,
    FontAwesomeModule
  ],
  templateUrl: './mailbox.component.html',
  styleUrl: './mailbox.component.css'
})
export class MailboxComponent implements OnInit{
  itemMailList:  ItemMailList;
  faTrashCan = faTrashCan;

  error_delete: boolean = false;
  error_delete_str: string = strings.ERROR_NOTIFICATIONS_DELETE;

  constructor(private api: ServerApiService,public themeService: ThemeService, private cookies: CookieService, private title: Title) {
    this.title.setTitle("Mailbox");
  }

  ngOnInit(): void{
    this.itemMailList = new ItemMailList();
    // Get the list of user's notifications.
    this.api.getNotifications(this.itemMailList);
  }

  remove(event: any){
    this.error_delete = false;
    var id = event.target.attributes.fill.ownerElement.parentNode.parentNode.parentNode.attributes.id.nodeValue;
    this.api.removeNotification(this.cookies.get('id_user') ,id).subscribe({
      next: data => {
        let json = JSON.parse(JSON.stringify(data));
        switch(json.response){
          case "0":
            this.ngOnInit();
            break;
          case "1":
            default:
            this.error_delete = true;
            break;
        }
      },
      error: error => {
        console.error(error);
        this.error_delete = true;
      }
    });
  }
}
