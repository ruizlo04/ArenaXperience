import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  private apiUrl = 'http://localhost:8080/review'; 

  constructor(private http: HttpClient, private authService: AuthService) { }

  private getAuthHeaders() {
    const token = this.authService.getToken();
    return { 
      headers: new HttpHeaders({
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }) 
    };
  }

  agregarReview(reviewData: { eventoName: string; rating: number; comment: string }): Observable<any> {
    const body = {
      eventoName: reviewData.eventoName.trim(),
      rating: Number(reviewData.rating),
      comment: reviewData.comment.trim()
    };

    return this.http.post(`${this.apiUrl}/agregar`, body, this.getAuthHeaders()).pipe(
      catchError(error => {
        if (error.status === 400 && error.error) {
          const validationErrors = error.error['invalid-params'] || [];
          const errorMessages = validationErrors.map((e: any) => e.message).join(', ');
          return throwError(() => new Error(errorMessages || 'Datos inválidos'));
        }
        return throwError(() => new Error(this.parseBackendError(error)));
      })
    );
  }

  private parseBackendError(error: any): string {
    if (error.error?.detail) {
      return error.error.detail;
    }
    if (error.error?.message) {
      return error.error.message;
    }
    if (error.error?.errors) {
      return Object.values(error.error.errors).join(', ');
    }
    return error.message || 'Error del servidor';
  }

  obtenerResenasPorEvento(eventoName: string, page: number = 0, size: number = 10): Observable<any> {
    return this.http.get(`${this.apiUrl}/evento/${encodeURIComponent(eventoName)}?page=${page}&size=${size}`, this.getAuthHeaders()).pipe(
      catchError(error => {
        return throwError(() => new Error(this.parseBackendError(error)));
      })
    );
  }

  editarResena(id: string, data: { rating: number; comment: string }): Observable<any> {
    return this.http.put(`${this.apiUrl}/editar/${id}`, data, this.getAuthHeaders()).pipe(
      catchError(error => {
        console.error('Error en editarResena:', error);
        return throwError(() => new Error(error.error?.message || 'Error al editar reseña'));
      })
    );
  }

  eliminarResena(id: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/eliminar/${id}`, this.getAuthHeaders()).pipe(
      catchError(error => {
        console.error('Error en eliminarResena:', error);
        return throwError(() => new Error(error.error?.message || 'Error al eliminar reseña'));
      })
    );
  }

  obtenerTodasLasResenas(page: number = 0, size: number = 10): Observable<any> {
    return this.http.get(`${this.apiUrl}/evento/?page=${page}&size=${size}`, this.getAuthHeaders()).pipe(
      catchError(error => {
        console.error('Error en obtenerTodasLasResenas:', error);
        return throwError(() => new Error(error.error?.message || 'Error al obtener todas las reseñas'));
      })
    );
  }
}