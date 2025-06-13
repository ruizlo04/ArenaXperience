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
    const cleanedFilters = {
      name: this.filters.name || null,
      date: this.filters.date || null,
      capacity: this.filters.capacity !== null ? this.filters.capacity : null
    };

    console.log('📦 Filtros aplicados:', cleanedFilters);

    this.filterApplied.emit(cleanedFilters);
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