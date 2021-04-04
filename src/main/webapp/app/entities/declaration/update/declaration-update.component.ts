import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IDeclaration, Declaration } from '../declaration.model';
import { DeclarationService } from '../service/declaration.service';

@Component({
  selector: 'jhi-declaration-update',
  templateUrl: './declaration-update.component.html',
})
export class DeclarationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    citizen: [null, [Validators.required]],
    passport: [null, [Validators.required]],
    foreignPassport: [null, [Validators.required]],
    passportNumber: [null, [Validators.required]],
    renouncedCitizenship: [null, [Validators.required]],
    passportSurrendered: [null, [Validators.required]],
    foreignPassportNumber: [null, [Validators.required]],
  });

  constructor(protected declarationService: DeclarationService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ declaration }) => {
      this.updateForm(declaration);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const declaration = this.createFromForm();
    if (declaration.id !== undefined) {
      this.subscribeToSaveResponse(this.declarationService.update(declaration));
    } else {
      this.subscribeToSaveResponse(this.declarationService.create(declaration));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeclaration>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(declaration: IDeclaration): void {
    this.editForm.patchValue({
      id: declaration.id,
      citizen: declaration.citizen,
      passport: declaration.passport,
      foreignPassport: declaration.foreignPassport,
      passportNumber: declaration.passportNumber,
      renouncedCitizenship: declaration.renouncedCitizenship,
      passportSurrendered: declaration.passportSurrendered,
      foreignPassportNumber: declaration.foreignPassportNumber,
    });
  }

  protected createFromForm(): IDeclaration {
    return {
      ...new Declaration(),
      id: this.editForm.get(['id'])!.value,
      citizen: this.editForm.get(['citizen'])!.value,
      passport: this.editForm.get(['passport'])!.value,
      foreignPassport: this.editForm.get(['foreignPassport'])!.value,
      passportNumber: this.editForm.get(['passportNumber'])!.value,
      renouncedCitizenship: this.editForm.get(['renouncedCitizenship'])!.value,
      passportSurrendered: this.editForm.get(['passportSurrendered'])!.value,
      foreignPassportNumber: this.editForm.get(['foreignPassportNumber'])!.value,
    };
  }
}
