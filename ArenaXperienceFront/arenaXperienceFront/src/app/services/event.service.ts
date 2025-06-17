import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
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

  buyTicket(eventName: string, token: string, cantidad: number): Observable<any> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const params = new HttpParams().set('cantidad', cantidad.toString());

    return this.http.post(
      `${this.baseUrl}/evento/${encodeURIComponent(eventName)}/comprar-ticket`,
      null,
      { headers, params }
    );
  }

  getEventoPorNombre(eventName: string, token: string): Observable<any> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get(`${this.baseUrl}/evento/nombre/${encodeURIComponent(eventName)}`, { headers });
  }

  getUserTickets(username: string, token: string) {
    return this.http.get<any[]>(`http://localhost:8080/user/${username}/tickets`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  crearEvento(evento: any, file: File, token: string): Observable<any> {
    const formData = new FormData();
    formData.append('event', new Blob([JSON.stringify(evento)], { type: 'application/json' }));
    formData.append('file', file);

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.post(`${this.baseUrl}/evento/register`, formData, { 
      headers,
      observe: 'response' 
    });
  }

}
