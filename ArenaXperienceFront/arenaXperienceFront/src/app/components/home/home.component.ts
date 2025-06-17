import { isPlatformBrowser } from '@angular/common';
import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { Router } from '@angular/router';
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
    private router: Router,
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
    this.currentPage = page;

    if (Object.keys(filters).length > 0) {
      this.eventService.searchEvents(this.token, filters).subscribe({
        next: (res) => {
          this.events = res;
          this.totalPages = 1;
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
    console.log('🛬 Filtros recibidos:', filters);
    this.getEvents(0, filters);
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
    this.router.navigate(['/ticket-list', eventName]);
  }

  editEvent(eventName: string): void {
    this.router.navigate(['/edit-event', eventName]);
  }

  isAdmin(): boolean {
    const role = localStorage.getItem('role');
    return role === 'ADMIN';
  }


}
