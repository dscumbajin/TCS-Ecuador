import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClienteService, Cliente } from '../../services/cliente';

@Component({
  selector: 'app-clientes',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './clientes.html',
  styleUrl: './clientes.scss',
})
export class ClientesComponent implements OnInit {
  clientes: Cliente[] = [];
  filteredClientes: Cliente[] = [];
  searchTerm: string = '';
  showModal: boolean = false;
  editingCliente: boolean = false;
  currentClienteId: number | null = null;
  
  successMessage: string = '';
  errorMessage: string = '';
  
  clienteForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private clienteService: ClienteService,
    private cdr: ChangeDetectorRef
  ) {
    this.clienteForm = this.fb.group({
      identificacion: ['', Validators.required],
      nombre: ['', Validators.required],
      genero: [''],
      edad: [0],
      direccion: ['', Validators.required],
      telefono: ['', Validators.required],
      contrasena: ['', Validators.required],
      estado: [true]
    });
  }

  ngOnInit(): void {
    this.loadClientes();
  }

  loadClientes(): void {
    this.clienteService.getClientes().subscribe({
      next: (data: Cliente[]) => {
        this.clientes = data || [];
        this.filteredClientes = this.clientes;
        this.cdr.detectChanges(); // Forzar actualización de la vista
      },
        error: (error: any) => {
          this.errorMessage = error.message || 'Error al cargar clientes';
          this.cdr.detectChanges();
        }
    });
  }

  filterClientes(): void {
    if (!this.searchTerm || this.searchTerm.trim() === '') {
      this.filteredClientes = [...this.clientes];
      return;
    }

    const term = this.searchTerm.toLowerCase().trim();
    this.filteredClientes = this.clientes.filter(cliente =>
      cliente.identificacion.toLowerCase().includes(term) ||
      cliente.nombre.toLowerCase().includes(term) ||
      cliente.direccion.toLowerCase().includes(term) ||
      cliente.telefono.toLowerCase().includes(term)
    );
  }

  openNewClienteModal(): void {
    this.editingCliente = false;
    this.currentClienteId = null;
    this.clienteForm.reset({
      identificacion: '',
      nombre: '',
      genero: '',
      edad: 0,
      direccion: '',
      telefono: '',
      contrasena: '',
      estado: true
    });
    this.showModal = true;
  }

  editCliente(cliente: Cliente): void {
  
    this.clienteService.getClienteByIdentificacion(cliente.identificacion).subscribe({
      next: (clienteCompleto: Cliente) => {
        this.editingCliente = true;
        this.currentClienteId = clienteCompleto.id || null;
        
        // Llenar el formulario con todos los datos
        this.clienteForm.patchValue({
          identificacion: clienteCompleto.identificacion,
          nombre: clienteCompleto.nombre,
          direccion: clienteCompleto.direccion,
          telefono: clienteCompleto.telefono,
          genero: clienteCompleto.genero || '',
          edad: clienteCompleto.edad || 0,
          contrasena: clienteCompleto.contrasena,
          estado: clienteCompleto.estado
        });
        
        this.showModal = true;
      },
      error: (error: any) => {
        this.errorMessage = 'Error al cargar los datos del cliente: ' + error.message;
        setTimeout(() => this.errorMessage = '', 5000);
      }
    });
  }

  saveCliente(): void {
    if (this.clienteForm.invalid) {
      this.markFormGroupTouched(this.clienteForm);
      return;
    }

    const clienteData: Cliente = this.clienteForm.value;

    if (this.editingCliente) {
      // Update existing cliente
      const clienteToUpdate: Cliente = {
        ...clienteData,
        id: this.currentClienteId || undefined // Puede ser undefined, el backend usará identificación
      };
      this.clienteService.updateCliente(clienteToUpdate).subscribe({
        next: () => {
          this.successMessage = 'Cliente actualizado exitosamente';
          this.loadClientes(); // Recargar la tabla
          this.closeModal(); // Cerrar el modal
          this.cdr.detectChanges(); // Forzar actualización de la vista
          setTimeout(() => this.successMessage = '', 5000);
        },
        error: (error: any) => {
          this.errorMessage = error.message || 'Error al actualizar el cliente';
          // No limpiar el mensaje automáticamente para que el usuario lo vea en el modal
        }
      });
    } else {
      // Create new cliente
      this.clienteService.createCliente(clienteData).subscribe({
        next: () => {
          this.successMessage = 'Cliente creado exitosamente';
          this.loadClientes();
          this.closeModal();
          setTimeout(() => this.successMessage = '', 5000);
        },
        error: (error: any) => {
          this.errorMessage = error.message || 'Error al crear el cliente';
          // No limpiar el mensaje automáticamente para que el usuario lo vea en el modal
        }
      });
    }
  }

  deleteCliente(identificacion: string): void {
    if (confirm('¿Está seguro de que desea eliminar este cliente?')) {
      this.clienteService.deleteCliente(identificacion).subscribe({
        next: () => {
          this.successMessage = 'Cliente eliminado exitosamente';
          this.loadClientes();
          setTimeout(() => this.successMessage = '', 5000);
        },
        error: (error: any) => {
          this.errorMessage = error.message || 'Error al eliminar el cliente';
          setTimeout(() => this.errorMessage = '', 5000);
        }
      });
    }
  }

  closeModal(): void {
    this.showModal = false;
    this.clienteForm.reset();
    this.editingCliente = false;
    this.currentClienteId = null;
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
