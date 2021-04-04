import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DeclarationComponent } from '../list/declaration.component';
import { DeclarationDetailComponent } from '../detail/declaration-detail.component';
import { DeclarationUpdateComponent } from '../update/declaration-update.component';
import { DeclarationRoutingResolveService } from './declaration-routing-resolve.service';

const declarationRoute: Routes = [
  {
    path: '',
    component: DeclarationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DeclarationDetailComponent,
    resolve: {
      declaration: DeclarationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DeclarationUpdateComponent,
    resolve: {
      declaration: DeclarationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DeclarationUpdateComponent,
    resolve: {
      declaration: DeclarationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(declarationRoute)],
  exports: [RouterModule],
})
export class DeclarationRoutingModule {}
