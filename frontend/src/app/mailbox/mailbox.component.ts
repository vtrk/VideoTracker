import { Component, OnInit } from '@angular/core';
import {NgClass} from "@angular/common";
import {ThemeService} from "../theme.service";
import {ItemListComponent} from "../item-list/item-list.component";
import { ItemList, } from '../utils/item';
import { CommonModule } from '@angular/common';
import {ServerApiService} from "../services/server-api.service";
@Component({
  selector: 'app-mailbox',
  standalone: true,
  imports: [
    ItemListComponent,
    CommonModule,
    NgClass
  ],
  templateUrl: './mailbox.component.html',
  styleUrl: './mailbox.component.css'
})
export class MailboxComponent {
  itemList: ItemList = new ItemList();
  constructor(private api: ServerApiService,public themeService: ThemeService) {}

  ngOnInit(): void{
    // Get the list of user's notifications.
    this.api.getDbNotifications(this.itemList);
  }
}
