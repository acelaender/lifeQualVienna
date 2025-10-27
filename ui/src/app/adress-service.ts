import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';

export interface LifeQualResponse {
  adress: string;
  userType: string;
  grade: number;
}

@Injectable({
  providedIn: 'root'
})
export class AdressService {
  private apiUrl = 'http://localhost:8080/api/lifequalvienna'

  constructor(private http: HttpClient) {
  }

  getGrade(adress: string, userType: string): Observable<LifeQualResponse> {
    const params = new HttpParams()
      .set('adress', adress)
      .set('userType', userType);
    return this.http.get<LifeQualResponse>(this.apiUrl, { params });
  }
}
