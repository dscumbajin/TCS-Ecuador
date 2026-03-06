import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MovimientoService, Movimiento } from '../../services/movimiento';
import { CuentaService } from '../../services/cuenta';
import { Cuenta } from '../../models/cuenta.interface';

@Component({
  selector: 'app-movimientos',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './movimientos.html',
  styleUrl: './movimientos.scss',
})
export class MovimientosComponent implements OnInit {
  movimientos: Movimiento[] = [];
  filteredMovimientos: Movimiento[] = [];
  cuentas: Cuenta[] = [];
  searchTerm: string = '';
  showModal: boolean = false;
  editingMovimiento: boolean = false;
  currentMovimientoNumero: string | null = null;
  
  successMessage: string = '';
  errorMessage: string = '';
  
  movimientoForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private movimientoService: MovimientoService,
    private cuentaService: CuentaService,
    private cdr: ChangeDetectorRef
  ) {
    this.movimientoForm = this.fb.group({
      numero: ['', Validators.required],
      tipo: ['', Validators.required],
      valor: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadCuentas();
    this.loadMovimientos();
  }

  loadCuentas(): void {
    this.cuentaService.getCuentas().subscribe({
      next: (cuentas) => {
        this.cuentas = cuentas;
        this.cdr.detectChanges();
      },
      error: (error) => {
        this.errorMessage = 'Error al cargar cuentas: ' + error.message;
        this.cdr.detectChanges();
      }
    });
  }

  loadMovimientos(): void {
    this.movimientoService.getMovimientos().subscribe({
      next: (data: Movimiento[]) => {
        this.movimientos = data || [];
        this.filteredMovimientos = this.movimientos;
        this.cdr.detectChanges(); // Forzar actualización de la vista
      },
      error: (error: any) => {
        this.errorMessage = error.message || 'Error al cargar movimientos';
        this.cdr.detectChanges();
      }
    });
  }

  filterMovimientos(): void {
    if (!this.searchTerm || this.searchTerm.trim() === '') {
      this.filteredMovimientos = [...this.movimientos];
      return;
    }

    const term = this.searchTerm.toLowerCase().trim();
    this.filteredMovimientos = this.movimientos.filter(movimiento =>
      movimiento.numero.toLowerCase().includes(term) ||
      movimiento.tipo.toLowerCase().includes(term) ||
      movimiento.valor.toLowerCase().includes(term)
    );
  }

  openNewMovimientoModal(): void {
    this.editingMovimiento = false;
    this.currentMovimientoNumero = null;
    this.movimientoForm.reset({
      numero: '',
      tipo: '',
      valor: ''
    });
    this.showModal = true;
  }

  editMovimiento(movimiento: Movimiento): void {
    this.movimientoService.getMovimientoByNumero(movimiento.numero).subscribe({
      next: (movimientoCompleto: Movimiento) => {
        this.editingMovimiento = true;
        this.currentMovimientoNumero = movimientoCompleto.numero;
        
        // Llenar el formulario con todos los datos
        this.movimientoForm.patchValue({
          numero: movimientoCompleto.numero,
          tipo: movimientoCompleto.tipo,
          saldo: movimientoCompleto.saldo,
          estado: movimientoCompleto.estado,
          valor: movimientoCompleto.valor
        });
        
        this.showModal = true;
      },
      error: (error: any) => {
        this.errorMessage = 'Error al cargar los datos del movimiento: ' + error.message;
        setTimeout(() => this.errorMessage = '', 5000);
      }
    });
  }

  saveMovimiento(): void {
    if (this.movimientoForm.invalid) {
      this.markFormGroupTouched(this.movimientoForm);
      return;
    }

    this.errorMessage = '';
    this.cdr.detectChanges();

    const movimientoData: Movimiento = this.movimientoForm.value;

    if (this.editingMovimiento) {
      // Update existing movimiento - include all fields
      const fullMovimientoData = {
        ...movimientoData,
        saldo: 0, 
        estado: true 
      };
      this.movimientoService.updateMovimiento(fullMovimientoData).subscribe({
        next: () => {
          this.successMessage = 'Movimiento actualizado exitosamente';
          this.loadMovimientos(); // Recargar la tabla
          this.closeModal(); // Cerrar el modal
          this.cdr.detectChanges(); // Forzar actualización de la vista
          setTimeout(() => this.successMessage = '', 5000);
        },
        error: (error: any) => {
          this.errorMessage = error.message || 'Error al actualizar el movimiento';
          this.cdr.detectChanges(); 
        }
      });
    } else {
      // Create new movimiento - only send required fields
      this.movimientoService.createMovimiento(movimientoData).subscribe({
        next: () => {
          this.successMessage = 'Movimiento creado exitosamente';
          this.loadMovimientos();
          this.closeModal();
          setTimeout(() => this.successMessage = '', 5000);
        },
        error: (error: any) => {
          this.errorMessage = error.message || 'Error al crear el movimiento';
          this.cdr.detectChanges();
        }
      });
    }
  }

  deleteMovimiento(numero: string): void {
    if (confirm('¿Está seguro de que desea eliminar este movimiento?')) {
      this.movimientoService.deleteMovimiento(numero).subscribe({
        next: () => {
          this.successMessage = 'Movimiento eliminado exitosamente';
          this.loadMovimientos();
          setTimeout(() => this.successMessage = '', 5000);
        },
        error: (error: any) => {
          this.errorMessage = error.message || 'Error al eliminar el movimiento';
          this.cdr.detectChanges();
          setTimeout(() => this.errorMessage = '', 5000);
        }
      });
    }
  }

  closeModal(): void {
    this.showModal = false;
    this.movimientoForm.reset();
    this.editingMovimiento = false;
    this.currentMovimientoNumero = null;
    this.errorMessage = ''; // Limpiar mensaje de error al cerrar el modal
  }

  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();
      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }
}
