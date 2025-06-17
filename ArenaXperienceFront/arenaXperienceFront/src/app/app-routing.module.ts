import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/auth/login/login.component';
import { NavInicioSesionComponent } from './components/navbars/nav-inicio-sesion/nav-inicio-sesion.component';
import { HomeComponent } from './components/home/home.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { ActivateAccountComponent } from './components/auth/activate-account/activate-account.component';
import { TicketListComponent } from './components/tickets/ticket-list/ticket-list.component';
import { MyTicketsComponent } from './components/tickets/my-tickets/my-tickets.component';
import { ProfileComponent } from './components/auth/profile/profile.component';
import { ChatComponent } from './components/chat/chat.component'; 
import { ReviewComponent } from './components/review/review.component';
import { CrearEventoComponent } from './components/crear-evento/crear-evento.component';

const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'nav-incio-sesion', component: NavInicioSesionComponent },
  { path: 'activate-account', component: ActivateAccountComponent },
  { path: 'ticket-list/:eventName', component: TicketListComponent },
  { path: 'my-tickets', component: MyTicketsComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'home', component: HomeComponent },
  { path: 'reviews/:eventName', component: ReviewComponent },
  { path: 'crear-evento', component: CrearEventoComponent },
  { path: 'chat', component: ChatComponent } 
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
