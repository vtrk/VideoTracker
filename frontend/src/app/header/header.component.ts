import { Component, OnInit } from '@angular/core';
import {RouterLink} from "@angular/router";
import {SearchService} from "../search.service";
import { HttpClient } from '@angular/common/http';
import { FormControl, FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-header',
  standalone: true,
    imports: [
        RouterLink,
        FormsModule,
        ReactiveFormsModule
    ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit{

  input: FormControl;

  private search: SearchService;

  constructor(private http: HttpClient) {}

  ngOnInit(): void{
    this.input = new FormControl('');
  }


  onSubmit(){
    //alert(this.input.value);
    console.log(this.search.sendSearchQuery(this.input.value, 'anime', new Map<string, string>))
  }
}
