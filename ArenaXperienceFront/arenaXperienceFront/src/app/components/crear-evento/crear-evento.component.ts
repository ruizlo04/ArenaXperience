import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EventService } from '../../services/event.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-crear-evento',
  templateUrl: './crear-evento.component.html',
  styleUrls: ['./crear-evento.component.css']
})
export class CrearEventoComponent {
  eventoForm: FormGroup;
  file: File | null = null;
  mensaje: string = '';
  submitted = false;

  constructor(
    private fb: FormBuilder,
    private eventoService: EventService,
    private router: Router
  ) {
    this.eventoForm = this.fb.group({
      name: ['', [
        Validators.required,
        Validators.minLength(7),
        Validators.pattern(/^(.{3,})-(.{3,})$/)
      ]],
      date: ['', [
        Validators.required,
        this.futureDateValidator
      ]],
      capacity: ['', [
        Validators.required,
        Validators.min(500),
        Validators.max(800)
      ]],
      price: ['', [
        Validators.required,
        Validators.min(0)
      ]]
    });
  }

  futureDateValidator(control: any) {
    const selectedDate = new Date(control.value);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    return selectedDate >= today ? null : { pastDate: true };
  }

  onFileChange(event: any): void {
    this.file = event.target.files[0];
  }

  onSubmit(): void {
    this.submitted = true;
    const token = localStorage.getItem('token');

    if (!token) {
      this.mensaje = 'No estás autenticado';
      return;
    }

    Object.values(this.eventoForm.controls).forEach(control => {
      control.markAsTouched();
    });

    if (this.eventoForm.valid && this.file) {
      this.eventoService.crearEvento(this.eventoForm.value, this.file, token).subscribe({
        next: () => {
          this.mensaje = 'Evento creado correctamente';

          this.eventoService.getEventsPaginated(token, 0, 10).subscribe((response) => {
          });

          setTimeout(() => {
            this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
              this.router.navigate(['/home']);
            });
          }, 1000);
        },
        error: (err) => {
          console.error('Error al crear evento:', err);
          this.mensaje = 'Error al crear el evento: ' + (err.error?.message || 'Ver consola para más detalles');
        }
      });
    } else {
      this.mensaje = 'Por favor, complete correctamente todos los campos';
    }
  }

  get f() {
    return this.eventoForm.controls;
  }
}