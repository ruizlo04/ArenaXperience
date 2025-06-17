import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EventService } from '../../services/event.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-edit-event',
  templateUrl: './edit-event.component.html',
  styleUrls: ['./edit-event.component.css']
})
export class EditEventComponent implements OnInit {
  eventForm: FormGroup;
  token: string = '';
  eventName: string = '';
  mensaje: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private eventService: EventService,
    private fb: FormBuilder
  ) {
    this.eventForm = this.fb.group({
      date: ['', Validators.required],
      capacity: ['', [Validators.required, Validators.min(500), Validators.max(800)]]
    });
  }

  ngOnInit(): void {
    this.token = localStorage.getItem('token') || '';
    this.eventName = this.route.snapshot.paramMap.get('name') || '';

    this.eventService.searchEvents(this.token, { nombre: this.eventName }).subscribe({
      next: (response) => {
        if (response.length > 0) {
          const event = response[0]; 
          this.eventForm.patchValue({
            date: event.date,
            capacity: event.capacity
          });
        } else {
          this.mensaje = 'Evento no encontrado';
        }
      },
      error: (err) => {
        console.error('Error al buscar evento:', err);
        this.mensaje = 'Error al buscar evento';
      }
    });

  }

  onSubmit(): void {
    if (this.eventForm.valid) {
      this.eventService.editarEvento(this.eventName, this.eventForm.value, this.token).subscribe({
        next: () => {
          this.mensaje = 'Evento actualizado correctamente'; 
          setTimeout(() => {
            this.router.navigate(['/home']);
          }, 1000);
        },
        error: (err) => {
          console.error('Error al editar evento:', err);
          this.mensaje = 'Error al actualizar el evento'; 
        }
      });
    }
  }
}
