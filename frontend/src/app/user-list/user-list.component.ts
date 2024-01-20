import { Component, OnInit } from '@angular/core';
import { NgClass } from "@angular/common";
import { ThemeService } from "../services/theme/theme.service";
import { CommonModule } from '@angular/common';
import { ServerApiService } from "../services/server-api/server-api.service";
import { CookieService } from 'ngx-cookie-service';
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { faTrashCan } from '@fortawesome/free-solid-svg-icons';
import { ItemUserList } from '../utils/item-user-list';
import { Router } from '@angular/router';
import { Title } from '@angular/platform-browser';
@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [
    NgClass,
    CommonModule,
    FontAwesomeModule
  ],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.css'
})
export class UserListComponent implements OnInit{
  userList: ItemUserList;
  faTrashCan = faTrashCan;

  delete_error: boolean = false;
  delete_error_str: string = "Error while deleting the item.";

  filter: string = 'all';

  constructor(private api: ServerApiService, public themeService: ThemeService, private cookies: CookieService, private router: Router, private title: Title) {
    this.title.setTitle("Video List");
  }

  ngOnInit(): void{
    this.userList = new ItemUserList();
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


  remove(event: any){
    this.delete_error = false;

    var id;
    if(event.target.attributes.id == undefined)
      if(event.target.attributes.fill == undefined)
        id = event.target.attributes.role.ownerElement.parentNode.attributes.id.nodeValue;
      else
        id = event.target.attributes.fill.ownerElement.parentNode.parentNode.attributes.id.nodeValue;
    else
      id = event.target.attributes.id.nodeValue;

    this.api.removeFromList(this.cookies.get('id_user'), id).subscribe({
      next: data => {
        let json = JSON.parse(JSON.stringify(data));
        switch(json.response){
          case "0":
            this.ngOnInit();
            break;
          case "1":
          default:
            this.delete_error = true;
            break;
        }
      },
      error: error => {
        console.error(error);
        this.delete_error = true;
      }
    });
  }

  retrieveUserList(){
    switch(this.filter){
      case 'all':
        return this.userList.getUserList();
      case 'plan to watch':
        return this.userList.getPlanToWatchList();
      case 'completed':
        return this.userList.getCompletedList();
      case 'watching':
        return this.userList.getWatchingList();
      case 'dropped':
        return this.userList.getDroppedList();
      case 'on hold':
        return this.userList.getOnHoldList();
      default:
        return this.userList.getUserList();
    }
  }

  filterAll(){
    this.filter = 'all';
  }

  filterWatching(){
    this.filter = 'watching';
  }

  filterPlanToWatch(){
    this.filter = 'plan to watch';
  }

  filterCompleted(){
    this.filter = 'completed';
  }

  filterDropped(){
    this.filter = 'dropped';
  }

  filterOnHold(){
    this.filter = 'on hold';
  }
}
