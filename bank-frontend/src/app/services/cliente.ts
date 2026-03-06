import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, map } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class ClienteService {
  private apiUrl = 'http://localhost:8081/api/clientes'; // Ajustar según la configuración del backend

  constructor(private http: HttpClient) {}

  getClientes(): Observable<Cliente[]> {
    return this.http.get<any>(this.apiUrl).pipe(
      catchError(this.handleError),
      map((response: any) => {
        
        // Intentar diferentes estructuras de respuesta comunes
        if (Array.isArray(response)) {
          return response;
        }
        
        // Si la respuesta tiene una propiedad 'data' o 'clientes'
        if (response && typeof response === 'object') {
          if (Array.isArray(response.data)) {
            return response.data;
          }
          if (Array.isArray(response.clientes)) {
            return response.clientes;
          }
          // Si es un objeto individual, convertirlo a array
          if (response.nombre) { // asumimos que si tiene nombre es un cliente
            return [response];
          }
        }
        
        return [];
      })
    );
  }

  getClienteById(id: number): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  getClienteByIdentificacion(identificacion: string): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.apiUrl}/cliente?identificacion=${identificacion}`).pipe(
      catchError(this.handleError)
    );
  }

  createCliente(cliente: Cliente): Observable<Cliente> {
    return this.http.post<Cliente>(this.apiUrl, cliente).pipe(
      catchError(this.handleError)
    );
  }

  updateCliente(cliente: Cliente): Observable<Cliente> {
    return this.http.put<Cliente>(this.apiUrl, cliente).pipe(
      catchError(this.handleError)
    );
  }

  deleteCliente(identificacion: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${identificacion}`).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Ocurrió un error desconocido';
    
    if (error.error instanceof ErrorEvent) {
      // Error del lado del cliente
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

export interface Cliente {
  id?: number;
  nombre: string;
  direccion: string;
  telefono: string;
  identificacion: string;
  genero?: string;
  edad?: number;
  contrasena: string;
  estado: boolean;
}
