import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';

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
    ).pipe(
      map(response => {
        localStorage.setItem('token', response.token);
        localStorage.setItem('refreshToken', response.refreshToken);
        return response;
      })
    );
  }

  getProfile(): Observable<any> {
    const token = this.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<any>(`${this.apiUrl}/user/me`, { headers });
  }

  getCurrentUser(token: string) {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<any>(`${this.apiUrl}/user/me`, { headers });
  }

  getToken(): string {
    if (typeof window !== 'undefined') {
      return localStorage.getItem('token') || '';
    }
    return '';
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
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

  getUsername(): string | null {
    if (typeof window !== 'undefined' && localStorage) {
      return localStorage.getItem('username');
    }
    return null;
  }

  editUser(username: string, data: { email: string; phoneNumber: string; password: string }) {
    const token = this.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.put<any>(`${this.apiUrl}/user/${username}`, data, { headers });
  }

  getAllUsers(page: number = 0, size: number = 3): Observable<any> {
    const token = this.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.get<any>(`${this.apiUrl}/user/?page=${page}&size=${size}`, { headers });
  }

  editUserByAdmin(username: string, data: {
    email: string;
    verifyEmail: string;
    phoneNumber: string;
    password: string;
    verifyPassword: string;
  }) {
    const token = this.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.put<any>(`${this.apiUrl}/user/admin/${username}`, {
      email: data.email,
      verifyEmail: data.verifyEmail,
      phoneNumber: data.phoneNumber,
      password: data.password,
      verifyPassword: data.verifyPassword
    }, { headers });
  }

  deleteUserByAdmin(username: string): Observable<any> {
    const token = this.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.delete(`${this.apiUrl}/user/delete/admin/${username}`, { headers });
  }
  
}
