import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router'; 
import { EventService } from '../../../services/event.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-ticket-list',
  templateUrl: './ticket-list.component.html',
  styleUrls: ['./ticket-list.component.css']
})
export class TicketListComponent implements OnInit {
  eventName: string = '';
  cantidad: number = 1;
  precioBase: number = 0;     
  precioFinal: number = 0;    

  constructor(
    private route: ActivatedRoute,
    private eventService: EventService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.eventName = this.route.snapshot.paramMap.get('eventName') || '';
    const token = this.authService.getToken();

    console.log('Token en ngOnInit:', token);  

    if (this.eventName) {
      this.eventService.getEventsPaginated(token, 0, 100).subscribe({
        next: (response) => {
          const evento = response.content.find((e: any) => e.name === this.eventName);
          if (evento) {
            this.precioBase = evento.price;
            this.actualizarPrecioFinal();
          } else {
            window.alert('❌ No se pudo encontrar la información del evento.');
          }
        },
        error: (error) => {
          console.error('Error al cargar eventos:', error);
          window.alert('❌ No se pudo cargar la información de los eventos.');
        }
      });
    } else {
      window.alert('❌ No se recibió el nombre del evento');
    }
  }

  validarCantidad(): void {
    if (this.cantidad < 1 || isNaN(this.cantidad)) {
      this.cantidad = 1;
    }
    this.actualizarPrecioFinal();
  }

  actualizarPrecioFinal(): void {
    this.precioFinal = this.precioBase * this.cantidad;
  }

  comprar(): void {
    const token = this.authService.getToken();

    this.eventService.buyTicket(this.eventName, token, this.cantidad).subscribe({
      next: (response) => {
        this.precioFinal = response.precioFinal; 
        alert(`✅ Compra exitosa para "${this.eventName}"\nCantidad: ${response.cantidad}\nTotal: €${this.precioFinal}`);

        setTimeout(() => {
          this.router.navigate(['/home']);
        }, 1000);
      },
      error: (error) => {
        console.error('Error al comprar ticket:', error);
        alert('❌ Ocurrió un error al realizar la compra.');
      }
    });
  }
}
