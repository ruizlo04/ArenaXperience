import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-filtro',
  templateUrl: './filtro.component.html',
  styleUrls: ['./filtro.component.css']
})
export class FiltroComponent {
  @Output() filterApplied = new EventEmitter<any>();
  
  filters = {
    name: '',
    date: '',
    capacity: null
  };

  applyFilters() {
    const cleanFilters = Object.fromEntries(
      Object.entries(this.filters).filter(([_, value]) => value !== '' && value !== null)
    );

    console.log('📦 Filtros aplicados:', cleanFilters); 

    this.filterApplied.emit(cleanFilters);
  }


  resetFilters() {
    this.filters = {
      name: '',
      date: '',
      capacity: null
    };
    this.filterApplied.emit({});
  }
}