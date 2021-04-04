import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { INextOfKeen, NextOfKeen } from '../next-of-keen.model';
import { NextOfKeenService } from '../service/next-of-keen.service';
import { IApplicant } from 'app/entities/applicant/applicant.model';
import { ApplicantService } from 'app/entities/applicant/service/applicant.service';
import { IAddress } from 'app/entities/address/address.model';
import { AddressService } from 'app/entities/address/service/address.service';

@Component({
  selector: 'jhi-next-of-keen-update',
  templateUrl: './next-of-keen-update.component.html',
})
export class NextOfKeenUpdateComponent implements OnInit {
  isSaving = false;

  applicantsCollection: IApplicant[] = [];
  addressesCollection: IAddress[] = [];

  editForm = this.fb.group({
    id: [],
    fullName: [null, [Validators.required]],
    relationshipToApplicant: [null, [Validators.required]],
    applicant: [null, Validators.required],
    address: [null, Validators.required],
  });

  constructor(
    protected nextOfKeenService: NextOfKeenService,
    protected applicantService: ApplicantService,
    protected addressService: AddressService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nextOfKeen }) => {
      this.updateForm(nextOfKeen);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const nextOfKeen = this.createFromForm();
    if (nextOfKeen.id !== undefined) {
      this.subscribeToSaveResponse(this.nextOfKeenService.update(nextOfKeen));
    } else {
      this.subscribeToSaveResponse(this.nextOfKeenService.create(nextOfKeen));
    }
  }

  trackApplicantById(index: number, item: IApplicant): number {
    return item.id!;
  }

  trackAddressById(index: number, item: IAddress): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INextOfKeen>>): void {
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

  protected updateForm(nextOfKeen: INextOfKeen): void {
    this.editForm.patchValue({
      id: nextOfKeen.id,
      fullName: nextOfKeen.fullName,
      relationshipToApplicant: nextOfKeen.relationshipToApplicant,
      applicant: nextOfKeen.applicant,
      address: nextOfKeen.address,
    });

    this.applicantsCollection = this.applicantService.addApplicantToCollectionIfMissing(this.applicantsCollection, nextOfKeen.applicant);
    this.addressesCollection = this.addressService.addAddressToCollectionIfMissing(this.addressesCollection, nextOfKeen.address);
  }

  protected loadRelationshipsOptions(): void {
    this.applicantService
      .query({ filter: 'nextofkeen-is-null' })
      .pipe(map((res: HttpResponse<IApplicant[]>) => res.body ?? []))
      .pipe(
        map((applicants: IApplicant[]) =>
          this.applicantService.addApplicantToCollectionIfMissing(applicants, this.editForm.get('applicant')!.value)
        )
      )
      .subscribe((applicants: IApplicant[]) => (this.applicantsCollection = applicants));

    this.addressService
      .query({ filter: 'nextofkeen-is-null' })
      .pipe(map((res: HttpResponse<IAddress[]>) => res.body ?? []))
      .pipe(
        map((addresses: IAddress[]) => this.addressService.addAddressToCollectionIfMissing(addresses, this.editForm.get('address')!.value))
      )
      .subscribe((addresses: IAddress[]) => (this.addressesCollection = addresses));
  }

  protected createFromForm(): INextOfKeen {
    return {
      ...new NextOfKeen(),
      id: this.editForm.get(['id'])!.value,
      fullName: this.editForm.get(['fullName'])!.value,
      relationshipToApplicant: this.editForm.get(['relationshipToApplicant'])!.value,
      applicant: this.editForm.get(['applicant'])!.value,
      address: this.editForm.get(['address'])!.value,
    };
  }
}
