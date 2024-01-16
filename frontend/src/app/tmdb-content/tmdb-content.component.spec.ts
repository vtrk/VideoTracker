import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TmdbContentComponent } from './tmdb-content.component';

describe('TmdbContentComponent', () => {
  let component: TmdbContentComponent;
  let fixture: ComponentFixture<TmdbContentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TmdbContentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TmdbContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
