import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KitsuContentComponent } from './kitsu-content.component';

describe('KitsuContentComponent', () => {
  let component: KitsuContentComponent;
  let fixture: ComponentFixture<KitsuContentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [KitsuContentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(KitsuContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
