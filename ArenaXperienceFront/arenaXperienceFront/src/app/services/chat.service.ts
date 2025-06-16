import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private apiUrl = 'http://localhost:8080/chat';

  constructor(private http: HttpClient) {}

 private getAuthHeaders(): HttpHeaders {
  const token = localStorage.getItem('token');
  if (token && token.length > 0) {
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  } else {
    return new HttpHeaders(); 
  }
}


  sendMessage(receiver: string, message: string): Observable<any> {
    const params = new HttpParams()
      .set('username', receiver)
      .set('message', message);
    return this.http.post(`${this.apiUrl}/send`, null, {
      params,
      headers: this.getAuthHeaders()
    });
  }


  getUserChats(): Observable<any> {
    return this.http.get(`${this.apiUrl}/list`, { headers: this.getAuthHeaders() });
  }

  editMessage(chatId: string, newMessage: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/edit/${chatId}`, { message: newMessage }, { headers: this.getAuthHeaders() });
  }

  deleteMessage(chatId: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete/${chatId}`, { headers: this.getAuthHeaders() });
  }
}
