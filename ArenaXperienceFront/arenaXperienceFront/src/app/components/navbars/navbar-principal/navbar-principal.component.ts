import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-navbar-principal',
  templateUrl: './navbar-principal.component.html',
  styleUrls: ['./navbar-principal.component.css']
})
export class NavbarPrincipalComponent implements OnInit {
  isSocioOrAdmin: boolean = false;
  currentUser: string = '';

  ngOnInit(): void {
    if (typeof window !== 'undefined' && window.localStorage) {
      const role = localStorage.getItem('role');
      this.isSocioOrAdmin = role === 'SOCIO' || role === 'ADMIN';

      const username = localStorage.getItem('username');
      if (username) {
        this.currentUser = username;
      }
    }
  }

  logout(): void {
    localStorage.clear();
    location.href = '/login';
  }
}
