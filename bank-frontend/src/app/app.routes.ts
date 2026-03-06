import { Routes } from '@angular/router';
import { LayoutComponent } from './components/layout/layout';
import { ClientesComponent } from './components/clientes/clientes';
import { CuentasComponent } from './components/cuentas/cuentas';
import { MovimientosComponent } from './components/movimientos/movimientos';
import { ReportesComponent } from './components/reportes/reportes';

export const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: '', redirectTo: '/clientes', pathMatch: 'full' },
      { path: 'clientes', component: ClientesComponent },
      { path: 'cuentas', component: CuentasComponent },
      { path: 'movimientos', component: MovimientosComponent },
      { path: 'reportes', component: ReportesComponent }
    ]
  }
];
