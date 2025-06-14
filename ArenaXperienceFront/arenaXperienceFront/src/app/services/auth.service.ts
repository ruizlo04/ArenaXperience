import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8080'; 

  constructor(private http: HttpClient) { }

  login(credentials: { username: string; password: string }) {
    return this.http.post<{ token: string; refreshToken: string }>(
      `${this.apiUrl}/user/auth/login`,
      credentials
    );
  }

  getCurrentUser(token: string) {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<any>(`${this.apiUrl}/user/me`, { headers });
  }

  register(data: any) {
    return this.http.post(`${this.apiUrl}/user/auth/register`, data);
  }

  activateAccount(data: { token: string }) {
    return this.http.post(`${this.apiUrl}/user/activate/account/`, data);
  }

  checkUsernameExists(username: string) {
    const params = new HttpParams().set('username', username);
    return this.http.get<{ exists: boolean }>(`${this.apiUrl}/user/auth/check-username`, { params }).pipe(
      map(response => response.exists)
    );
  }
}
