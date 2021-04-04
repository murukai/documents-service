import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDemographicDetails } from '../demographic-details.model';
import { DemographicDetailsService } from '../service/demographic-details.service';

@Component({
  templateUrl: './demographic-details-delete-dialog.component.html',
})
export class DemographicDetailsDeleteDialogComponent {
  demographicDetails?: IDemographicDetails;

  constructor(protected demographicDetailsService: DemographicDetailsService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.demographicDetailsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
