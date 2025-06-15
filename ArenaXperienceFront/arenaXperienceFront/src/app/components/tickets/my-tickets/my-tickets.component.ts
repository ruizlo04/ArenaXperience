import { Component, OnInit } from '@angular/core';
import { EventService } from '../../../services/event.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-my-tickets',
  templateUrl: './my-tickets.component.html',
  styleUrls: ['./my-tickets.component.css']
})
export class MyTicketsComponent implements OnInit {
  tickets: any[] = [];
  username: string = '';

  constructor(
    private eventService: EventService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const token = this.authService.getToken();
    this.username = localStorage.getItem('username') || ''; 

    console.log('Username:', this.username);
    console.log('Token:', token);

    this.eventService.getUserTickets(this.username, token).subscribe({
      next: (response) => {
        this.tickets = response;
      },
      error: (error) => {
        console.error('Error al cargar las entradas:', error);
        alert('❌ No se pudieron cargar tus entradas.');
      }
    });
  }
}
