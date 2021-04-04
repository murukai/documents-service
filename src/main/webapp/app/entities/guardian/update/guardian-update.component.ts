import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IGuardian, Guardian } from '../guardian.model';
import { GuardianService } from '../service/guardian.service';

@Component({
  selector: 'jhi-guardian-update',
  templateUrl: './guardian-update.component.html',
})
export class GuardianUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    fullName: [null, [Validators.required]],
    idNumber: [null, [Validators.required]],
    relationshipToApplicant: [null, [Validators.required]],
  });

  constructor(protected guardianService: GuardianService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ guardian }) => {
      this.updateForm(guardian);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const guardian = this.createFromForm();
    if (guardian.id !== undefined) {
      this.subscribeToSaveResponse(this.guardianService.update(guardian));
    } else {
      this.subscribeToSaveResponse(this.guardianService.create(guardian));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGuardian>>): void {
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

  protected updateForm(guardian: IGuardian): void {
    this.editForm.patchValue({
      id: guardian.id,
      fullName: guardian.fullName,
      idNumber: guardian.idNumber,
      relationshipToApplicant: guardian.relationshipToApplicant,
    });
  }

  protected createFromForm(): IGuardian {
    return {
      ...new Guardian(),
      id: this.editForm.get(['id'])!.value,
      fullName: this.editForm.get(['fullName'])!.value,
      idNumber: this.editForm.get(['idNumber'])!.value,
      relationshipToApplicant: this.editForm.get(['relationshipToApplicant'])!.value,
    };
  }
}
