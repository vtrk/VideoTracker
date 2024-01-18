import { Component, OnInit } from '@angular/core';
import { NgClass } from "@angular/common";
import { ThemeService } from "../theme.service";
import { ItemListComponent } from "../item-list/item-list.component";
import { CommonModule } from '@angular/common';
import { ServerApiService } from "../services/server-api-service/server-api.service";
import { CookieService } from 'ngx-cookie-service';
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { faTrashCan } from '@fortawesome/free-solid-svg-icons';
import { ItemUserList } from '../utils/item-user-list';
import { Router } from '@angular/router';
@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [
    NgClass,
    ItemListComponent,
    CommonModule,
    FontAwesomeModule
  ],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.css'
})
export class UserListComponent implements OnInit{
  userList: ItemUserList = new ItemUserList();
  faTrashCan = faTrashCan;


  //user: User;
  constructor(private api: ServerApiService, public themeService: ThemeService, private cookies: CookieService, private router: Router) {}

  ngOnInit(): void{
    // Get the list of items.
    this.api.getDbList(this.userList, this.cookies.get('id_user'));
  }

  goto(event: any){
    var target = event.target;
    var id = target.attributes.id.nodeValue;
    var type = target.attributes.title.nodeValue;
    switch(type){
      case 'movie':
      case 'tv':
        this.router.navigate(['/tmdb', type, id]);
        break;
      case 'anime':
        this.router.navigate(['/kitsu', type, id]);
        break;
      default:
        break;
    }
  }

  //protected readonly User = User;

  remove(event: any){
    var target = event.target;
    var type = target.attributes.title.nodeValue;
    var id = target.attributes.id.nodeValue;
    this.api.removeFromList(id,type);
    switch(type){
      case 'movie':
      case 'tv':
        this.router.navigate(['/tmdb', type, id]);
        break;
      case 'anime':
        this.router.navigate(['/kitsu', type, id]);
        break;
      default:
        break;
    }
  }
}
