import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

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
}
