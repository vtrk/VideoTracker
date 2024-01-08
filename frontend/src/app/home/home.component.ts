import { Component, OnInit } from '@angular/core';
import { Item } from '../item';
import { ServerApiService } from '../server-api.service';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})


export class HomeComponent implements OnInit{
  items: Item[] = []; 

  constructor(private api: ServerApiService) {}

  ngOnInit(): void{
    this.getTrendings();
  }

  getTrendings(){
    this.api.getTrending("anime").subscribe({
      next: data => {
        let json = JSON.parse(JSON.stringify(data));
        console.log(json);
        json.data.forEach((element: any) => {
          let item: Item = {
            id: 0,
            type: '',
            name: '',
            image: ''
          };
          item.id = element.id;
          item.type = element.type;
          item.name = element.attributes.canonicalTitle;
          item.image = element.attributes.posterImage.original;
          this.items.push(item);
          console.log(item);
        }); 
        return;
      },
      error: error => {
        console.log(error);
        return;
      }
    })
  }

  /**
   * TODO: Loads the content of the item clicked on.
   * @param event 
   */
  goto(event: any){
    var target = event.target;
    var value = target.attributes.id.nodeValue;
    var type = target.attributes.title.nodeValue;
    console.log(value);
    console.log(type);
  }
}
