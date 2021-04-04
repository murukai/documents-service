import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPassport, Passport } from '../passport.model';
import { PassportService } from '../service/passport.service';

@Component({
  selector: 'jhi-passport-update',
  templateUrl: './passport-update.component.html',
})
export class PassportUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    passportNumber: [null, [Validators.required]],
    issuedAt: [null, [Validators.required]],
    issuedDate: [null, [Validators.required]],
    lossDescription: [null, [Validators.required]],
  });

  constructor(protected passportService: PassportService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ passport }) => {
      if (passport.id === undefined) {
        const today = dayjs().startOf('day');
        passport.issuedDate = today;
      }

      this.updateForm(passport);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const passport = this.createFromForm();
    if (passport.id !== undefined) {
      this.subscribeToSaveResponse(this.passportService.update(passport));
    } else {
      this.subscribeToSaveResponse(this.passportService.create(passport));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPassport>>): void {
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

  protected updateForm(passport: IPassport): void {
    this.editForm.patchValue({
      id: passport.id,
      passportNumber: passport.passportNumber,
      issuedAt: passport.issuedAt,
      issuedDate: passport.issuedDate ? passport.issuedDate.format(DATE_TIME_FORMAT) : null,
      lossDescription: passport.lossDescription,
    });
  }

  protected createFromForm(): IPassport {
    return {
      ...new Passport(),
      id: this.editForm.get(['id'])!.value,
      passportNumber: this.editForm.get(['passportNumber'])!.value,
      issuedAt: this.editForm.get(['issuedAt'])!.value,
      issuedDate: this.editForm.get(['issuedDate'])!.value ? dayjs(this.editForm.get(['issuedDate'])!.value, DATE_TIME_FORMAT) : undefined,
      lossDescription: this.editForm.get(['lossDescription'])!.value,
    };
  }
}
