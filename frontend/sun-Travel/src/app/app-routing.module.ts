import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddContractComponent } from './core/pages/add-contract/add-contract.component';
import { AddHotelComponent } from './core/pages/add-hotel/add-hotel.component';
import { DashboardComponent } from './core/pages/dashboard/dashboard.component';
import { ViewContractComponent } from './core/pages/view-contract/view-contract.component';

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'addHotel', component: AddHotelComponent },
  { path: 'addContract', component: AddContractComponent },
  { path: 'viewContract', component: ViewContractComponent },
  { path: '**', redirectTo: '/dashboard', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
