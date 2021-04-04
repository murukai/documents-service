import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeclaration } from '../declaration.model';
import { DeclarationService } from '../service/declaration.service';

@Component({
  templateUrl: './declaration-delete-dialog.component.html',
})
export class DeclarationDeleteDialogComponent {
  declaration?: IDeclaration;

  constructor(protected declarationService: DeclarationService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.declarationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
