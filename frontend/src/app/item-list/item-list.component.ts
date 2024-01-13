import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ItemList } from '../utils/item';

@Component({
  selector: 'item-list',
  standalone: true,
  imports: [CommonModule],
  template: `
        <div class="container mx-auto mt-4">
          <div class="row">
            <div *ngFor="let item of itemList.getItems()" class="col-md-4">
              <div class="d-flex aligns-items-center justify-content-center card text-center w-75 mx-auto" style="width: 18rem;">
                <img id="{{item.id}}" title="{{item.type}}" src="{{item.image}}" alt="404" (click)="goto($event)" class="card-img-top" style="height: 400px!important; object-fit: cover;"/><br>
                <div class="card-body">
                  <label id="{{item.id}}" title="{{item.type}}" for="{{item.id}}" (click)="goto($event)" class="card-title">{{item.name}}</label>
                </div>
              </div>
            </div>
          </div>
        </div>
  `
})
export class ItemListComponent {
    @Input() itemList: ItemList;

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
