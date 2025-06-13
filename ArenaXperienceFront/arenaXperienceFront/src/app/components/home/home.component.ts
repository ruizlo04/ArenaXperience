// home.component.ts
import { isPlatformBrowser } from '@angular/common';
import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { EventService } from '../../services/event.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  events: any[] = [];
  currentPage: number = 0;
  totalPages: number = 0;
  pageSize: number = 5;
  menuOpen: boolean = false;
  token: string = '';
  currentFilters: any = {};

  constructor(
    private eventService: EventService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.token = localStorage.getItem('token') || '';
      if (this.token) {
        this.getEvents(this.currentPage);
      } else {
        console.warn('Token no encontrado');
      }
    }
  }

  getEvents(page: number, filters: any = {}): void {
    if (Object.keys(filters).length > 0) {
      this.eventService.searchEvents(this.token, filters).subscribe({
        next: (res) => {
          // Asumiendo que la respuesta de búsqueda es un array
          this.events = res;
          this.totalPages = 1; // La búsqueda no tiene paginación en este ejemplo
        },
        error: (err) => {
          console.error('Error al buscar eventos:', err);
        }
      });
    } else {
      this.eventService.getEventsPaginated(this.token, page, this.pageSize).subscribe({
        next: (res) => {
          this.events = res.content;
          this.totalPages = res.totalPages;
        },
        error: (err) => {
          console.error('Error al obtener eventos:', err);
        }
      });
    }
  }

  onFilterApplied(filters: any): void {
    this.currentFilters = filters;
    this.currentPage = 0;
    this.getEvents(this.currentPage, filters);
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.getEvents(this.currentPage, this.currentFilters);
    }
  }

  prevPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.getEvents(this.currentPage, this.currentFilters);
    }
  }

  buyTicket(eventName: string): void {
    alert(`Comprar entrada para: ${eventName}`);
  }

  logout(): void {
    localStorage.clear();
    location.href = '/login';
  }
}