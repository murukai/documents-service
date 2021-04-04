import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { DemographicDetailsComponent } from './list/demographic-details.component';
import { DemographicDetailsDetailComponent } from './detail/demographic-details-detail.component';
import { DemographicDetailsUpdateComponent } from './update/demographic-details-update.component';
import { DemographicDetailsDeleteDialogComponent } from './delete/demographic-details-delete-dialog.component';
import { DemographicDetailsRoutingModule } from './route/demographic-details-routing.module';

@NgModule({
  imports: [SharedModule, DemographicDetailsRoutingModule],
  declarations: [
    DemographicDetailsComponent,
    DemographicDetailsDetailComponent,
    DemographicDetailsUpdateComponent,
    DemographicDetailsDeleteDialogComponent,
  ],
  entryComponents: [DemographicDetailsDeleteDialogComponent],
})
export class DemographicDetailsModule {}
