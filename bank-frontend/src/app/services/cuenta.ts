import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, map } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Cuenta } from '../models/cuenta.interface';

@Injectable({
  providedIn: 'root',
})
export class CuentaService {
  private apiUrl = 'http://localhost:8082/api/cuentas';

  constructor(private http: HttpClient) {}

  getCuentas(): Observable<Cuenta[]> {
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
          if (Array.isArray(response.cuentas)) {
            return response.cuentas;
          }

          if (response.numero) { 
            return [response];
          }
        }
        
        return [];
      })
    );
  }

  getCuentaByNumero(numero: string): Observable<Cuenta> {
    return this.http.get<Cuenta>(`${this.apiUrl}/cuenta?numero=${numero}`).pipe(
      catchError(this.handleError)
    );
  }

  createCuenta(cuenta: Cuenta): Observable<Cuenta> {
    return this.http.post<Cuenta>(this.apiUrl, cuenta).pipe(
      catchError(this.handleError)
    );
  }

  updateCuenta(cuenta: Cuenta): Observable<Cuenta> {
    return this.http.put<Cuenta>(this.apiUrl, cuenta).pipe(
      catchError(this.handleError)
    );
  }

  deleteCuenta(numero: string): Observable<void> {
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
      
      // Si el backend envía un mensaje de error específico
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
