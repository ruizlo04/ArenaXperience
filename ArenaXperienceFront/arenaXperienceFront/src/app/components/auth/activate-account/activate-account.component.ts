import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-activate-account',
  templateUrl: './activate-account.component.html',
  styleUrls: ['./activate-account.component.css']
})
export class ActivateAccountComponent {
  tokenForm: FormGroup;
  error = '';
  success = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.tokenForm = this.fb.group({
      token: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.tokenForm.valid) {
      this.authService.activateAccount(this.tokenForm.value).subscribe({
        next: () => {
          this.success = 'Cuenta activada correctamente 🎉';
          setTimeout(() => this.router.navigate(['/login']), 2000);
        },
        error: () => {
          this.error = 'Token inválido o expirado.';
        }
      });
    }
  }
}
