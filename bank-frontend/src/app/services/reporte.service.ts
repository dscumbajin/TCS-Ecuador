import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, map } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Reporte, ReporteResponse } from '../models/reporte.interface';

@Injectable({
  providedIn: 'root',
})
export class ReporteService {
  private apiUrl = 'http://localhost:8082/api/reportes';

  constructor(private http: HttpClient) {}

  getReportes(nombre: string, fechaInicio: string, fechaFin: string): Observable<Reporte[]> {
    const params = new URLSearchParams();
    if (nombre) params.append('nombre', nombre);
    if (fechaInicio) params.append('fechaInicio', this.formatDate(fechaInicio));
    if (fechaFin) params.append('fechaFin', this.formatDate(fechaFin));

    const url = `${this.apiUrl}?${params.toString()}`;
    
    return this.http.get<ReporteResponse>(url).pipe(
      catchError(this.handleError),
      map((response: ReporteResponse) => {
        return response.data || [];
      })
    );
  }

  private formatDate(dateString: string): string {
    if (!dateString) return '';
    
    // HTML date input provides date in YYYY-MM-DD format
    // We need to convert it to dd/MM/yyyy format
    const [year, month, day] = dateString.split('-');
    
    // Ensure two-digit format for day and month
    const formattedDay = day.padStart(2, '0');
    const formattedMonth = month.padStart(2, '0');
    
    return `${formattedDay}/${formattedMonth}/${year}`;
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Ocurrió un error desconocido';
    
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Error: ${error.error.message}`;
    } else {
      errorMessage = `Error del servidor: ${error.status}, ${error.message}`;
      
      if (error.error && typeof error.error === 'object') {
        if (error.error.error) {
          errorMessage = error.error.error;
        } else if (error.error.message) {
          errorMessage = error.error.message;
        }
      }
    }
    
    return throwError(() => new Error(errorMessage));
  }
}
