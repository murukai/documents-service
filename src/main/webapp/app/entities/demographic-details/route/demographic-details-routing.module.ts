import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DemographicDetailsComponent } from '../list/demographic-details.component';
import { DemographicDetailsDetailComponent } from '../detail/demographic-details-detail.component';
import { DemographicDetailsUpdateComponent } from '../update/demographic-details-update.component';
import { DemographicDetailsRoutingResolveService } from './demographic-details-routing-resolve.service';

const demographicDetailsRoute: Routes = [
  {
    path: '',
    component: DemographicDetailsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DemographicDetailsDetailComponent,
    resolve: {
      demographicDetails: DemographicDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DemographicDetailsUpdateComponent,
    resolve: {
      demographicDetails: DemographicDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DemographicDetailsUpdateComponent,
    resolve: {
      demographicDetails: DemographicDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(demographicDetailsRoute)],
  exports: [RouterModule],
})
export class DemographicDetailsRoutingModule {}
