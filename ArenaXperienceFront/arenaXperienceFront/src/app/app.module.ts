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
import { RegisterComponent } from './components/auth/register/register.component';
import { ActivateAccountComponent } from './components/auth/activate-account/activate-account.component';
import { TicketListComponent } from './components/tickets/ticket-list/ticket-list.component';
import { MyTicketsComponent } from './components/tickets/my-tickets/my-tickets.component';
import { ProfileComponent } from './components/auth/profile/profile.component';
import { ChatComponent } from './components/chat/chat.component';
import { ReviewComponent } from './components/review/review.component';
import { CrearEventoComponent } from './components/crear-evento/crear-evento.component';
import { EditEventComponent } from './components/edit-event/edit-event.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    NavInicioSesionComponent,
    HomeComponent,
    NavbarPrincipalComponent,
    FiltroComponent,
    RegisterComponent,
    ActivateAccountComponent,
    TicketListComponent,
    MyTicketsComponent,
    ProfileComponent,
    ChatComponent,
    ReviewComponent,
    CrearEventoComponent,
    EditEventComponent
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
