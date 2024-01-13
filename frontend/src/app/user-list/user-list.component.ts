import { Component, OnInit } from '@angular/core';
import {NgClass} from "@angular/common";
import {ThemeService} from "../theme.service";
import {ItemListComponent} from "../item-list/item-list.component";
import { ItemList, } from '../utils/item';
import { CommonModule } from '@angular/common';
import {ServerApiService} from "../services/server-api.service";

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
  constructor(private api: ServerApiService, public themeService: ThemeService) {}

  ngOnInit(): void{
    // Get the list of items.
    this.api.getDbList(this.itemList);
  }

}
