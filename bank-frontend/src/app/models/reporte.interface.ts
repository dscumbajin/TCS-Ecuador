export interface Reporte {
  fecha: string;
  cliente: string;
  numeroCuenta: string;
  tipo: string;
  saldoInicial: number;
  estado: boolean;
  movimiento: string;
  saldoDisponible: number;
}

export interface ReporteResponse {
  data: Reporte[];
}
