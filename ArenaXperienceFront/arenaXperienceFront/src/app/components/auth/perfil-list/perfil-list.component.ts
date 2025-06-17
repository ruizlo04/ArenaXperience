import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-perfil-list',
  templateUrl: './perfil-list.component.html',
  styleUrls: ['./perfil-list.component.css']
})
export class PerfilListComponent implements OnInit {
  allUsers: any[] = [];
  loadingUsers = true;
  usersError = '';
  currentPage = 0;
  totalPages = 0;

  editedUser: any = null;

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    this.loadAllUsers();
  }

  loadAllUsers(page: number = 0): void {
    this.loadingUsers = true;
    this.usersError = '';
    this.authService.getAllUsers(page, 3).subscribe({
      next: (response) => {
        this.allUsers = response.content;
        this.totalPages = response.totalPages;
        this.currentPage = response.number;
        this.loadingUsers = false;
      },
      error: () => {
        this.usersError = 'No se pudieron cargar los usuarios.';
        this.loadingUsers = false;
      }
    });
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.loadAllUsers(page);
    }
  }

  startEditUser(user: any): void {
    this.editedUser = {
      username: user.username,
      email: user.email,
      verifyEmail: user.email,
      phoneNumber: user.phoneNumber,
      password: '',
      verifyPassword: ''
    };
  }

  saveEditedUser(): void {
    if (!this.editedUser) return;

    if (this.editedUser.email !== this.editedUser.verifyEmail) {
      alert('Los correos no coinciden.');
      return;
    }
    if (this.editedUser.password !== this.editedUser.verifyPassword) {
      alert('Las contraseñas no coinciden.');
      return;
    }

    this.authService.editUserByAdmin(this.editedUser.username, {
      email: this.editedUser.email,
      verifyEmail: this.editedUser.verifyEmail,
      phoneNumber: this.editedUser.phoneNumber,
      password: this.editedUser.password || '',
      verifyPassword: this.editedUser.verifyPassword || ''
    }).subscribe({
      next: () => {
        alert('Usuario actualizado correctamente');
        this.editedUser = null;
        this.loadAllUsers(this.currentPage);
      },
      error: err => {
        console.error(err);
        alert('Error al actualizar usuario');
      }
    });
  }

  confirmDelete(username: string): void {
    if (confirm(`¿Estás seguro de que deseas eliminar al usuario ${username}? Esta acción no se puede deshacer.`)) {
      this.deleteUser(username);
    }
  }

  deleteUser(username: string): void {
    this.authService.deleteUserByAdmin(username).subscribe({
      next: () => {
        alert('Usuario eliminado correctamente');
        this.loadAllUsers(this.currentPage);
      },
      error: (err) => {
        console.error(err);
        alert('Error al eliminar usuario');
      }
    });
  }

  cancelEdit(): void {
    this.editedUser = null;
  }
}
