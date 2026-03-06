export interface Cuenta {
  numero: string;
  tipoCuenta: string;
  saldoInicial: number;
  estado: boolean;
  nombre: string;
  identificacion: string;
}

export interface CuentaResponse {
  data: Cuenta[];
}
