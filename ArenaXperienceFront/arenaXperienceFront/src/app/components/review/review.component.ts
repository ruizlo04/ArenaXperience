import { Component, Input, OnInit } from '@angular/core';
import { ReviewService } from '../../services/review.service';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../../services/auth.service'; // Importa AuthService

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.css']
})
export class ReviewComponent implements OnInit {
  eventName: string = '';
  mensajeExito: string = '';
  mensajeError: string = '';
  reviews: any[] = [];
  newReview = { rating: 0, comment: '' };
  isLoading = false;
  currentPage = 0;
  pageSize = 5;
  totalPages = 0;
  totalReviews = 0;
  currentUser: any;

  constructor(
    private reviewService: ReviewService,
    private route: ActivatedRoute,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.authService.getProfile().subscribe({
      next: (user) => {
        this.currentUser = user;
        this.route.paramMap.subscribe(params => {
          this.eventName = params.get('eventName') || '';
          if (this.eventName) {
            this.getReviews();
          } else {
            this.mensajeError = 'No se ha especificado un evento válido';
          }
        });
      },
      error: (err) => {
        this.mensajeError = 'Error al obtener el usuario actual';
        console.error(err);
      }
    });
  }


  wordCount(): number {
    return this.newReview.comment ? this.newReview.comment.trim().split(/\s+/).length : 0;
  }

  isFormValid(): boolean {
    return this.eventName.trim() !== '' &&
      this.newReview.rating >= 0 &&
      this.newReview.rating <= 10 &&
      this.newReview.comment.trim() !== '' &&
      this.wordCount() <= 150;
  }

  getReviews(): void {
    this.isLoading = true;
    this.reviewService.obtenerResenasPorEvento(this.eventName, this.currentPage, this.pageSize).subscribe({
      next: (res: any) => {
        this.reviews = res.content || [];
        this.totalPages = res.totalPages || 1;
        this.totalReviews = res.totalElements || 0;
        this.isLoading = false;
      },
      error: (err) => {
        this.mensajeError = err.message || 'Error al cargar reseñas';
        setTimeout(() => this.mensajeError = '', 5000);
        this.isLoading = false;
      }
    });
  }

  addReview(): void {
    if (!this.isFormValid()) {
      this.mensajeError = 'Por favor completa todos los campos correctamente';
      setTimeout(() => this.mensajeError = '', 3000);
      return;
    }

    this.isLoading = true;
    this.mensajeError = '';

    const reviewData = {
      eventoName: this.eventName.trim(),
      rating: this.newReview.rating,
      comment: this.newReview.comment.trim()
    };

    this.reviewService.agregarReview(reviewData).subscribe({
      next: () => {
        this.mensajeExito = '¡Reseña publicada con éxito!';
        this.newReview = { rating: 0, comment: '' };
        this.currentPage = 0;
        this.getReviews();
        setTimeout(() => this.mensajeExito = '', 3000);
      },
      error: (err) => {
        this.mensajeError = err.message || 'Error al enviar reseña';
        setTimeout(() => this.mensajeError = '', 5000);
        this.isLoading = false;
      }
    });
  }

  eliminarReview(id: string): void {
    if (!confirm('¿Estás seguro de que deseas eliminar esta reseña?')) return;

    this.isLoading = true;
    this.reviewService.eliminarResena(id).subscribe({
      next: () => {
        this.mensajeExito = 'Reseña eliminada exitosamente.';
        this.getReviews();
        setTimeout(() => this.mensajeExito = '', 3000);
      },
      error: (err) => {
        this.mensajeError = err.message || 'Error al eliminar reseña';
        setTimeout(() => this.mensajeError = '', 5000);
        this.isLoading = false;
      }
    });
  }

  changePage(page: number): void {
    this.currentPage = page;
    this.getReviews();
  }

  getPagesArray(): number[] {
    return Array(this.totalPages).fill(0).map((x, i) => i);
  }
}
