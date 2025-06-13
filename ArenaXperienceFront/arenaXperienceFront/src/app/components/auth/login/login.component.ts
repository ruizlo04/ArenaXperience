import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  error: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.authService.login(this.loginForm.value).subscribe({
        next: (res) => {
          localStorage.setItem('token', res.token);
          localStorage.setItem('refreshToken', res.refreshToken);

          // Obtener info del usuario
          this.authService.getCurrentUser(res.token).subscribe({
            next: (user) => {
              const username = user.name || user.username;
              localStorage.setItem('username', username);

              // Asignar rol manualmente según usuario
              let role = '';
              if (username === 'admin') role = 'ADMIN';
              else if (['user3', 'user6'].includes(username)) role = 'SOCIO';
              else role = 'USER';

              localStorage.setItem('role', role);

              this.router.navigate(['/home']);
            },
            error: (err) => {
              console.error('Error al obtener datos de usuario:', err);
              // En caso de error, aún puedes asumir el rol por el username que se usó para loguear
              const username = this.loginForm.value.username;
              localStorage.setItem('username', username);

              let role = '';
              if (username === 'admin') role = 'ADMIN';
              else if (['user3', 'user6'].includes(username)) role = 'SOCIO';
              else role = 'USER';

              localStorage.setItem('role', role);

              this.router.navigate(['/home']);
            }
          });
        },
        error: (err) => {
          if (err.status === 401 || err.status === 404) {
            this.error = err.error?.message || 'Usuario o contraseña incorrectos';
          } else {
            this.error = 'Error del servidor. Inténtalo más tarde.';
          }
        }
      });
    }
  }
}
