import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidatorFn, AsyncValidatorFn } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { Observable, of } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap, map, catchError } from 'rxjs/operators';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerForm: FormGroup;
  error: string = '';
  fieldErrors: { [key: string]: string } = {};

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      username: ['', 
        [Validators.required],
        [this.uniqueUsernameValidator()]
      ],
      email: ['', [Validators.required, Validators.email]],
      verifyEmail: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', [Validators.required, Validators.pattern(/^\d{9}$/)]],
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.maxLength(20),
          Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{8,20}$/)
        ]
      ],
      verifyPassword: ['', Validators.required]
    }, {
      validators: [
        this.matchFields('email', 'verifyEmail', 'emailsMismatch'),
        this.matchFields('password', 'verifyPassword', 'passwordsMismatch')
      ]
    });
  }

  uniqueUsernameValidator(): AsyncValidatorFn {
    return (control: AbstractControl): Observable<{ [key: string]: any } | null> => {
      if (!control.value) {
        return of(null);
      }
      return of(control.value).pipe(
        debounceTime(500),
        switchMap(username =>
          this.authService.checkUsernameExists(username).pipe(
            map(isTaken => (isTaken ? { usernameTaken: true } : null)),
            catchError(() => of(null))
          )
        )
      );
    };
  }

  matchFields(field1: string, field2: string, errorKey: string): ValidatorFn {
    return (group: AbstractControl) => {
      const value1 = group.get(field1)?.value;
      const value2 = group.get(field2)?.value;

      if (value1 !== value2) {
        group.get(field2)?.setErrors({ ...(group.get(field2)?.errors || {}), [errorKey]: true });
      } else {
        const errors = group.get(field2)?.errors;
        if (errors) {
          delete errors[errorKey];
          group.get(field2)?.setErrors(Object.keys(errors).length ? errors : null);
        }
      }
      return null;
    };
  }

  getError(field: string): string | null {
    const control = this.registerForm.get(field);

    if (this.fieldErrors[field]) {
      return this.fieldErrors[field];
    }

    if (control && control.invalid && (control.dirty || control.touched)) {
      if (control.hasError('required')) {
        return 'Este campo es obligatorio.';
      }

      if (control.hasError('email')) {
        return 'El correo no tiene formato válido.';
      }

      if (control.hasError('emailsMismatch')) {
        return 'Los correos electrónicos no coinciden.';
      }

      if (control.hasError('passwordsMismatch')) {
        return 'Las contraseñas no coinciden.';
      }

      if (field === 'password') {
        if (control.hasError('minlength') || control.hasError('maxlength')) {
          return 'La contraseña debe tener entre 8 y 20 caracteres.';
        }

        if (control.hasError('pattern')) {
          return 'La contraseña debe tener al menos una mayúscula, una minúscula, un número y un carácter especial.';
        }
      }

      if (field === 'phoneNumber') {
        return 'El número de teléfono debe tener exactamente 9 dígitos.';
      }

      if (field === 'username' && control.hasError('usernameTaken')) {
        return 'El nombre de usuario ya está en uso.';
      }
    }

    return null;
  }

  onSubmit() {
    this.error = '';
    this.fieldErrors = {};

    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      this.error = ''; 
      return;
    }

    const form = this.registerForm.value;

    const requestBody = {
      username: form.username,
      email: form.email,
      verifyEmail: form.verifyEmail,
      password: form.password,
      verifyPassword: form.verifyPassword,
      phoneNumber: form.phoneNumber
    };

    this.authService.register(requestBody).subscribe({
      next: () => {
        alert('Registro exitoso. Verifica tu correo para activar la cuenta.');
        this.router.navigate(['/activate-account']);
      },
      error: (err) => {
        console.error('Error en el registro:', err);

        if (err.error?.['invalid-params']) {
          err.error['invalid-params'].forEach((e: any) => {
            if (e.name) {
              this.fieldErrors[e.name] = e.reason;
            }
          });
          this.error = 'Hay errores de validación en el formulario.';
        } else {
          this.error = err.error?.message || err.error?.detail || 'Error en el registro. Revisa los datos.';
        }
      }
    });
  }
}
