import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReporteService } from '../../services/reporte.service';
import { CuentaService } from '../../services/cuenta';
import { Cuenta } from '../../models/cuenta.interface';
import { Reporte } from '../../models/reporte.interface';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

@Component({
  selector: 'app-reportes',
  imports: [CommonModule, FormsModule],
  templateUrl: './reportes.html',
  styleUrl: './reportes.scss',
})
export class ReportesComponent implements OnInit {
  cuentas: Cuenta[] = [];
  reportes: Reporte[] = [];
  selectedCliente: string = '';
  fechaInicio: string = '';
  fechaFin: string = '';
  isLoading: boolean = false;
  error: string = '';

  constructor(
    private reporteService: ReporteService,
    private cuentaService: CuentaService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadCuentas();
  }

  loadCuentas(): void {
    this.cuentaService.getCuentas().subscribe({
      next: (cuentas: Cuenta[]) => {
        this.cuentas = cuentas;
        this.cdr.detectChanges(); // Forzar actualización de la vista
      },
      error: (error: any) => {
        this.error = 'Error al cargar cuentas: ' + error.message;
        this.cdr.detectChanges();
      }
    });
  }

  generarReporte(): void {
    if (!this.selectedCliente || !this.fechaInicio || !this.fechaFin) {
      this.error = 'Por favor complete todos los campos';
      this.cdr.detectChanges();
      return;
    }

    this.isLoading = true;
    this.error = '';
    this.reportes = [];
    this.reporteService.getReportes(this.selectedCliente, this.fechaInicio, this.fechaFin).subscribe({
      next: (reportes) => {
        this.reportes = reportes;
        this.isLoading = false;
        
        if (reportes.length === 0) {
          this.error = 'No se encontraron movimientos para el cliente ' + this.selectedCliente + ' en el rango de fechas seleccionado';
        }
        
        this.cdr.detectChanges(); 
      },
      error: (error) => {
        console.error('API Error:', error);
        this.error = error.message || 'Error al generar reporte';
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  limpiar(): void {
    this.selectedCliente = '';
    this.fechaInicio = '';
    this.fechaFin = '';
    this.reportes = [];
    this.error = '';
    this.cdr.detectChanges();
  }

  exportToPDF(): void {
    if (this.reportes.length === 0) {
      this.error = 'No hay datos para exportar';
      this.cdr.detectChanges();
      return;
    }

    const doc = new jsPDF();
    
    // Título del reporte
    doc.setFontSize(16);
    doc.text('Reporte de Movimientos', 14, 20);
    
    // Información del filtro
    doc.setFontSize(10);
    doc.text(`Cliente: ${this.selectedCliente}`, 14, 30);
    doc.text(`Fecha Inicio: ${this.formatDateForDisplay(this.fechaInicio)}`, 14, 37);
    doc.text(`Fecha Fin: ${this.formatDateForDisplay(this.fechaFin)}`, 14, 44);
    doc.text(`Fecha de Generación: ${new Date().toLocaleString()}`, 14, 51);
    
    // Tabla de datos
    const tableData = this.reportes.map(reporte => [
      reporte.fecha,
      reporte.cliente,
      reporte.numeroCuenta,
      reporte.tipo,
      `$${reporte.saldoInicial.toFixed(2)}`,
      reporte.estado ? 'Activa' : 'Inactiva',
      `$${reporte.movimiento}`,
      `$${reporte.saldoDisponible.toFixed(2)}`
    ]);
    
    autoTable(doc, {
      head: [['Fecha', 'Cliente', 'N° Cuenta', 'Tipo', 'Saldo Inicial', 'Estado', 'Movimiento', 'Saldo Disponible']],
      body: tableData,
      startY: 60,
      theme: 'grid',
      styles: { fontSize: 8 },
      headStyles: { fillColor: [66, 139, 202] }
    });
    
    // Resumen
    const finalY = (doc as any).lastAutoTable.finalY || 60;
    doc.setFontSize(10);
    doc.text(`Total de Movimientos: ${this.reportes.length}`, 14, finalY + 10);
    
    // Guardar el PDF
    const fileName = `reporte_${this.selectedCliente.replace(/\s+/g, '_')}_${this.fechaInicio}_${this.fechaFin}.pdf`;
    doc.save(fileName);
  }

  private formatDateForDisplay(dateString: string): string {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleDateString('es-ES');
  }
}
