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

  constructor(private authService: AuthService) {}

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
}
