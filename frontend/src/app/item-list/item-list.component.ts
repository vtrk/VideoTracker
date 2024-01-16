import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ItemList } from '../utils/item';
import { Router } from '@angular/router';

@Component({
  selector: 'item-list',
  standalone: true,
  imports: [CommonModule],
  template: `
        <div class="container mx-auto mt-4">
          <div class="row">
            <div *ngFor="let item of itemList.getItems()" class="col-md-4">
              <div class="d-flex aligns-items-center justify-content-center card text-center w-75 mx-auto" style="width: 18rem; cursor: pointer;">
                <img id="{{item.id}}" title="{{item.type}}" src="{{item.image}}" alt="404" (click)="goto($event)" class="card-img-top" style="height: 400px!important; object-fit: cover;"/><br>
                <div class="card-body">
                  <label id="{{item.id}}" title="{{item.type}}" for="{{item.id}}" (click)="goto($event)" class="card-title" style="cursor: pointer;">{{item.name}}</label>
                </div>
              </div>
            </div>
          </div>
        </div>
  `
})
export class ItemListComponent {
    @Input() itemList: ItemList;

    constructor(private router: Router) {}

    /**
   * TODO: Loads the content of the item clicked on.
   * @param event
   */
    goto(event: any){
      var target = event.target;
      var id = target.attributes.id.nodeValue;
      var type = target.attributes.title.nodeValue;
      switch(type){
        case 'movie':
        case 'tv':
          this.router.navigate(['/content', type, id]);
          break;
        case 'anime':
          //TODO
          break;
        default:
          break;
      }
    }
}
