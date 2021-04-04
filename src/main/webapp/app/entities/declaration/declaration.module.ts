import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { DeclarationComponent } from './list/declaration.component';
import { DeclarationDetailComponent } from './detail/declaration-detail.component';
import { DeclarationUpdateComponent } from './update/declaration-update.component';
import { DeclarationDeleteDialogComponent } from './delete/declaration-delete-dialog.component';
import { DeclarationRoutingModule } from './route/declaration-routing.module';

@NgModule({
  imports: [SharedModule, DeclarationRoutingModule],
  declarations: [DeclarationComponent, DeclarationDetailComponent, DeclarationUpdateComponent, DeclarationDeleteDialogComponent],
  entryComponents: [DeclarationDeleteDialogComponent],
})
export class DeclarationModule {}
