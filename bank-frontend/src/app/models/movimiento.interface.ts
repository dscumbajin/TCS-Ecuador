export interface Movimiento {
  numero: string;
  tipo: string;
  saldo: number;
  estado: boolean;
  valor: string;
}

export interface MovimientoResponse {
  data: Movimiento[];
}
