import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CuentaService } from '../../services/cuenta';
import { ClienteService, Cliente } from '../../services/cliente';
import { Cuenta } from '../../models/cuenta.interface';

@Component({
  selector: 'app-cuentas',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './cuentas.html',
  styleUrl: './cuentas.scss',
})
export class CuentasComponent implements OnInit {
  cuentas: Cuenta[] = [];
  filteredCuentas: Cuenta[] = [];
  clientes: Cliente[] = [];
  searchTerm: string = '';
  showModal: boolean = false;
  editingCuenta: boolean = false;
  currentCuentaNumero: string | null = null;
  
  successMessage: string = '';
  errorMessage: string = '';
  
  cuentaForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private cuentaService: CuentaService,
    private clienteService: ClienteService,
    private cdr: ChangeDetectorRef
  ) {
    this.cuentaForm = this.fb.group({
      numero: ['', Validators.required],
      tipoCuenta: ['', Validators.required],
      saldoInicial: [0, [Validators.required, Validators.min(0)]],
      estado: [true],
      identificacion: ['']
    });
  }

  ngOnInit(): void {
    this.loadCuentas();
    this.loadClientes();
  }

  loadCuentas(): void {
    this.cuentaService.getCuentas().subscribe({
      next: (data: Cuenta[]) => {
        this.cuentas = data || [];
        this.filteredCuentas = this.cuentas;
        this.cdr.detectChanges(); // Forzar actualización de la vista
      },
      error: (error: any) => {
        this.errorMessage = error.message || 'Error al cargar las cuentas';
        this.cdr.detectChanges();
      }
    });
  }

  loadClientes(): void {
    this.clienteService.getClientes().subscribe({
      next: (data: Cliente[]) => {
        this.clientes = data || [];
        this.cdr.detectChanges();
      },
      error: (error: any) => {
        console.error('Error al cargar clientes:', error);
      }
    });
  }

  filterCuentas(): void {
    if (!this.searchTerm || this.searchTerm.trim() === '') {
      this.filteredCuentas = [...this.cuentas];
      return;
    }

    const term = this.searchTerm.toLowerCase().trim();
    this.filteredCuentas = this.cuentas.filter(cuenta =>
      cuenta.numero.toLowerCase().includes(term) ||
      cuenta.nombre.toLowerCase().includes(term) ||
      cuenta.tipoCuenta.toLowerCase().includes(term)
    );
  }

  openNewCuentaModal(): void {
    this.editingCuenta = false;
    this.currentCuentaNumero = null;
    this.cuentaForm.reset({
      numero: '',
      tipoCuenta: '',
      saldoInicial: 0,
      estado: true,
      identificacion: ''
    });
    
    // Agregar validación requerida para identificación al crear
    this.cuentaForm.get('identificacion')?.setValidators(Validators.required);
    
    this.showModal = true;
  }

  editCuenta(cuenta: Cuenta): void {
    // Para edición, no necesitamos llamar al API, usamos los datos directamente
    this.editingCuenta = true;
    this.currentCuentaNumero = cuenta.numero;
    
    // Remover validación de identificación al editar
    this.cuentaForm.get('identificacion')?.clearValidators();
    
    // Llenar el formulario con los datos existentes
    this.cuentaForm.patchValue({
      numero: cuenta.numero,
      tipoCuenta: cuenta.tipoCuenta,
      saldoInicial: cuenta.saldoInicial,
      estado: cuenta.estado,
      identificacion: cuenta.identificacion
    });
    
    this.showModal = true;
  }

  saveCuenta(): void {
    if (this.cuentaForm.invalid) {
      this.markFormGroupTouched(this.cuentaForm);
      return;
    }

    const cuentaData: Cuenta = this.cuentaForm.value;

    if (this.editingCuenta) {
      // Update existing cuenta
      const updateData: Partial<Cuenta> = {
        numero: cuentaData.numero,
        tipoCuenta: cuentaData.tipoCuenta,
        saldoInicial: cuentaData.saldoInicial,
        estado: cuentaData.estado
      };
      
      this.cuentaService.updateCuenta(updateData as Cuenta).subscribe({
        next: () => {
          this.successMessage = 'Cuenta actualizada exitosamente';
          this.loadCuentas(); // Recargar la tabla
          this.closeModal(); // Cerrar el modal
          this.cdr.detectChanges(); // Forzar actualización de la vista
          setTimeout(() => this.successMessage = '', 5000);
        },
        error: (error: any) => {
          this.errorMessage = error.message || 'Error al actualizar la cuenta';
          // No limpiar el mensaje automáticamente para que el usuario lo vea en el modal
        }
      });
    } else {
      // Create new cuenta - enviar todos los campos incluyendo identificacion
      this.cuentaService.createCuenta(cuentaData).subscribe({
        next: () => {
          this.successMessage = 'Cuenta creada exitosamente';
          this.loadCuentas();
          this.closeModal();
          setTimeout(() => this.successMessage = '', 5000);
        },
        error: (error: any) => {
          this.errorMessage = error.message || 'Error al crear la cuenta';
        }
      });
    }
  }

  deleteCuenta(numero: string): void {
    if (confirm('¿Está seguro de que desea eliminar esta cuenta?')) {
      this.cuentaService.deleteCuenta(numero).subscribe({
        next: () => {
          this.successMessage = 'Cuenta eliminada exitosamente';
          this.loadCuentas();
          setTimeout(() => this.successMessage = '', 5000);
        },
        error: (error: any) => {
          this.errorMessage = error.message || 'Error al eliminar la cuenta';
          setTimeout(() => this.errorMessage = '', 5000);
        }
      });
    }
  }

  closeModal(): void {
    this.showModal = false;
    this.cuentaForm.reset();
    this.editingCuenta = false;
    this.currentCuentaNumero = null;
    this.errorMessage = '';
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
