import {Component, inject, signal} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {HttpClientModule} from '@angular/common/http';
import { provideHttpClient } from '@angular/common/http';
import {AdressService, LifeQualResponse, Place} from './adress-service';
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

  suggestions: Place[] = [];
  selectedPlace?: Place;

  userType = 'student';
  result?: LifeQualResponse;
  error?: string;

  onSubmit(event: Event) {
    event.preventDefault();

    // Clear previous state
    this.suggestions = [];
    this.selectedPlace = undefined;
    this.result = undefined;
    this.error = undefined;

    this.adressService.getPlace(this.adress)
      .subscribe({
        next: (res) => this.suggestions = res,
        error: (err) => this.error = err.error?.error() || 'Could not fetch adresses'
      });
  }

  selectPlace(place: Place) {
    this.selectedPlace = place;
  }

  onCalculate() {
    if(!this.selectedPlace) {
      this.error = 'Please choose one of the provided adresses first'
      return;
    }

    this.adressService.getGrade(this.selectedPlace, this.userType).subscribe({
      next: (res) => this.result = res,
      error: () => (this.error = 'Could not fetch quality')
    });

  }
}
