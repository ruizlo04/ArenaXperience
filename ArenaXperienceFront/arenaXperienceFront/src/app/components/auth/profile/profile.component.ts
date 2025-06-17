import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  user: any = null;
  editedUser: any = {};
  loading = true;
  error = '';
  editing = false;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile(): void {
    this.authService.getProfile().subscribe({
      next: (data) => {
        this.user = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'No se pudo cargar el perfil.';
        this.loading = false;
      }
    });
  }

  toggleEdit(): void {
    this.editing = !this.editing;
    if (this.editing) {
      this.editedUser = {
        email: this.user.email,
        verifyEmail: this.user.email,
        password: '',
        verifyPassword: '',
        phoneNumber: this.user.phoneNumber
      };
    }
  }

  saveChanges(): void {
    if (this.editedUser.email !== this.editedUser.verifyEmail) {
      alert('Los emails no coinciden.');
      return;
    }

    if (this.editedUser.password || this.editedUser.verifyPassword) {
      if (this.editedUser.password !== this.editedUser.verifyPassword) {
        alert('Las contraseñas no coinciden.');
        return;
      }
    }

    const username = this.user.username;

    const updatedData = {
      email: this.editedUser.email,
      verifyEmail: this.editedUser.verifyEmail,
      phoneNumber: this.editedUser.phoneNumber,
      password: this.editedUser.password || '', 
      verifyPassword: this.editedUser.verifyPassword || '' 
    };

    this.authService.editUser(username, updatedData).subscribe({
      next: (updatedUser) => {
        this.user = updatedUser;
        this.editing = false;
        alert('Perfil actualizado correctamente');
      },
      error: (err) => {
        console.error(err);
        alert('Hubo un error al actualizar el perfil.');
      }
    });
  }

}
