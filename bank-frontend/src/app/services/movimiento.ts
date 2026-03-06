import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, map } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Movimiento, MovimientoResponse } from '../models/movimiento.interface';

@Injectable({
  providedIn: 'root',
})
export class MovimientoService {
  private apiUrl = 'http://localhost:8082/api/movimientos';

  constructor(private http: HttpClient) {}

  getMovimientos(): Observable<Movimiento[]> {
    return this.http.get<any>(this.apiUrl).pipe(
      catchError(this.handleError),
      map((response: any) => {
        
        if (Array.isArray(response)) {
          return response;
        }
        
        if (response && typeof response === 'object') {
          if (Array.isArray(response.data)) {
            return response.data;
          }
          if (Array.isArray(response.movimientos)) {
            return response.movimientos;
          }

          if (response.numero) {
            return [response];
          }
        }
        
        return [];
      })
    );
  }

  getMovimientoByNumero(numero: string): Observable<Movimiento> {
    return this.http.get<Movimiento>(`${this.apiUrl}/movimiento?numero=${numero}`).pipe(
      catchError(this.handleError)
    );
  }

  createMovimiento(movimiento: Movimiento): Observable<Movimiento> {
    return this.http.post<Movimiento>(this.apiUrl, movimiento).pipe(
      catchError(this.handleError)
    );
  }

  updateMovimiento(movimiento: Movimiento): Observable<Movimiento> {
    return this.http.put<Movimiento>(this.apiUrl, movimiento).pipe(
      catchError(this.handleError)
    );
  }

  deleteMovimiento(numero: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${numero}`).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Ocurrió un error desconocido';
    
    if (error.error instanceof ErrorEvent) {

      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Error del lado del servidor
      errorMessage = `Error del servidor: ${error.status}, ${error.message}`;

      if (error.error && typeof error.error === 'object') {
        if (error.error.error) {
          errorMessage = error.error.error; // Mostrar el mensaje exacto del API
        } else if (error.error.message) {
          errorMessage = error.error.message;
        } else if (typeof error.error === 'string') {
          errorMessage = error.error; // Si el error es un string directo
        }
      } else if (typeof error.error === 'string') {
        errorMessage = error.error; // Si el error es un string directo
      }
    }
    
    return throwError(() => new Error(errorMessage));
  }
}

export type { Movimiento, MovimientoResponse } from '../models/movimiento.interface';
