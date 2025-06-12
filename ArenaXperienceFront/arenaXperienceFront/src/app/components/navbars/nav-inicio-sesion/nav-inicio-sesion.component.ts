import { Component } from '@angular/core';
import { Router } from '@angular/router';


@Component({
  selector: 'app-nav-inicio-sesion',
  templateUrl: './nav-inicio-sesion.component.html',
  styleUrl: './nav-inicio-sesion.component.css'
})
export class NavInicioSesionComponent {

  constructor(
      private router: Router
  ){}

  goToRegister() {
    this.router.navigate(['/register']);
  }

}
