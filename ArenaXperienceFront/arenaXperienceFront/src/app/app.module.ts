import { NgModule } from '@angular/core';
import { BrowserModule} from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/auth/login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { provideHttpClient } from '@angular/common/http';
import { NavInicioSesionComponent } from './components/navbars/nav-inicio-sesion/nav-inicio-sesion.component';
import { RouterModule } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { NavbarPrincipalComponent } from './components/navbars/navbar-principal/navbar-principal.component';
import { FiltroComponent } from './components/filters/filtro/filtro.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    NavInicioSesionComponent,
    HomeComponent,
    NavbarPrincipalComponent,
    FiltroComponent
  ],
  imports: [
    RouterModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [
    provideHttpClient()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
