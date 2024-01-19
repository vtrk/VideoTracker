import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})

export class ThemeService {
  private darkTheme: boolean = false;

  constructor() {
  }

  public toggleTheme(): void {
    this.darkTheme = !this.darkTheme;
    if (this.darkTheme) {
      document.body.classList.add('dark-theme');
    } else {
      document.body.classList.remove('dark-theme');
    }
  }

  public isDarkTheme(): boolean {
    return this.darkTheme;
  }

}
