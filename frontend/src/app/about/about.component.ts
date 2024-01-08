import { Component, OnInit } from '@angular/core';
import {ChangeDetectorRef} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { ServerAPIResponse } from '../data-structures';

@Component({
  selector: 'app-about',
  standalone: true,
  imports: [],
  templateUrl: './about.component.html',
  styleUrl: './about.component.css'
})
export class AboutComponent implements OnInit{
  title = 'about';
  serverAPI: string;
  serverVersion: string;

  ngOnInit(): void {
    this.ref.detectChanges();
  }

  constructor(private ref:ChangeDetectorRef, private client: HttpClient) {
    this.client.get<ServerAPIResponse>(environment.API_URL + '/api').subscribe(data => {
      this.serverAPI = "Server API: " + data.API;
      this.serverVersion = "Server Version: " + data.Version;
    });
  }
}