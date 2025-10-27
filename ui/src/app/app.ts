import {Component, inject, signal} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {HttpClientModule} from '@angular/common/http';
import { provideHttpClient } from '@angular/common/http';
import {AdressService, LifeQualResponse} from './adress-service';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    HttpClientModule,
    FormsModule,
  ],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  adressService = inject(AdressService);
  adress = '';
  result?: LifeQualResponse;
  error?: string;

  onSubmit(event: Event) {
    event.preventDefault();
    this.adressService.getGrade(this.adress, "test")
      .subscribe({
        next: (res) => this.result = res,
        error: (err) => this.error = err.error?.error() || 'Error'
      });
  }


}
