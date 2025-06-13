import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getEventsPaginated(token: string, page: number, size: number): Observable<any> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get(`${this.baseUrl}/evento/?page=${page}&size=${size}`, { headers });
  }

  searchEvents(token: string, filters: any): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    return this.http.post(`${this.baseUrl}/evento/search`, filters, { headers });
  }

  buyTicket(eventName: string, token: string): Observable<any> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post(`${this.baseUrl}/evento/${encodeURIComponent(eventName)}/comprar-ticket`, {}, { headers });
  }
}