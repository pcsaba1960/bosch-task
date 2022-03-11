import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AboutComponent } from './module/about/about/about.component';
import { CenterComponent } from './module/center/center/center.component';
import { ProductionComponent } from './module/production/production/production.component';

const routes: Routes = [
  {
    path: 'production',
    component: ProductionComponent
  },
  {
    path: 'center',
    component: CenterComponent
  },
  {
    path: 'about',
    component: AboutComponent
  },
]
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
