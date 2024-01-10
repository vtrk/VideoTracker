import {TestBed} from '@angular/core/testing';
import {AppComponent} from './app.component';
import {ThemeService} from "./theme.service";

describe('ThemeService', () => {
  let service: ThemeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ThemeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
