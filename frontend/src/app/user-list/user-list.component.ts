import { Component, OnInit } from '@angular/core';
import {NgClass} from "@angular/common";
import {ThemeService} from "../theme.service";
import {ItemListComponent} from "../item-list/item-list.component";
import { ItemList, } from '../utils/item';
import { CommonModule } from '@angular/common';
import {ServerApiService} from "../services/server-api.service";
import { CookieService } from 'ngx-cookie-service';
import {User} from "../utils/user";

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [
    NgClass,
    ItemListComponent,
    CommonModule
  ],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.css'
})
export class UserListComponent implements OnInit{
  itemList: ItemList = new ItemList();
  email: string;
  username: string;
  completed: string;
  watching: string;
  stopped: string;
  planned: string;
  info: Array<string>;
  //user: User;
  constructor(private api: ServerApiService, public themeService: ThemeService, private cookies: CookieService) {}

  ngOnInit(): void{
    // Get the list of items.
    this.api.getDbList(this.itemList, this.cookies.get('id_user'));
  }

  //protected readonly User = User;
}
