import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavInicioSesionComponent } from './nav-inicio-sesion.component';

describe('NavInicioSesionComponent', () => {
  let component: NavInicioSesionComponent;
  let fixture: ComponentFixture<NavInicioSesionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NavInicioSesionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NavInicioSesionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
