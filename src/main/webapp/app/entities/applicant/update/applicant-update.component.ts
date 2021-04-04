import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IApplicant, Applicant } from '../applicant.model';
import { ApplicantService } from '../service/applicant.service';
import { IDemographicDetails } from 'app/entities/demographic-details/demographic-details.model';
import { DemographicDetailsService } from 'app/entities/demographic-details/service/demographic-details.service';
import { IDeclaration } from 'app/entities/declaration/declaration.model';
import { DeclarationService } from 'app/entities/declaration/service/declaration.service';
import { IGuardian } from 'app/entities/guardian/guardian.model';
import { GuardianService } from 'app/entities/guardian/service/guardian.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IAppointmentSlot } from 'app/entities/appointment-slot/appointment-slot.model';
import { AppointmentSlotService } from 'app/entities/appointment-slot/service/appointment-slot.service';

@Component({
  selector: 'jhi-applicant-update',
  templateUrl: './applicant-update.component.html',
})
export class ApplicantUpdateComponent implements OnInit {
  isSaving = false;

  democraphicDetailsCollection: IDemographicDetails[] = [];
  declarationsCollection: IDeclaration[] = [];
  guardiansCollection: IGuardian[] = [];
  usersSharedCollection: IUser[] = [];
  appointmentSlotsSharedCollection: IAppointmentSlot[] = [];

  editForm = this.fb.group({
    id: [],
    surname: [null, [Validators.required]],
    otherNames: [null, [Validators.required]],
    maidenName: [null, [Validators.required]],
    changedName: [null, [Validators.required]],
    gender: [],
    maritalStatus: [],
    dateOfBirth: [null, [Validators.required]],
    idNumber: [null, [Validators.required]],
    birthEntryNumber: [null, [Validators.required]],
    democraphicDetails: [null, Validators.required],
    declaration: [],
    guardian: [],
    user: [null, Validators.required],
    appointmentSlot: [],
  });

  constructor(
    protected applicantService: ApplicantService,
    protected demographicDetailsService: DemographicDetailsService,
    protected declarationService: DeclarationService,
    protected guardianService: GuardianService,
    protected userService: UserService,
    protected appointmentSlotService: AppointmentSlotService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ applicant }) => {
      this.updateForm(applicant);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const applicant = this.createFromForm();
    if (applicant.id !== undefined) {
      this.subscribeToSaveResponse(this.applicantService.update(applicant));
    } else {
      this.subscribeToSaveResponse(this.applicantService.create(applicant));
    }
  }

  trackDemographicDetailsById(index: number, item: IDemographicDetails): number {
    return item.id!;
  }

  trackDeclarationById(index: number, item: IDeclaration): number {
    return item.id!;
  }

  trackGuardianById(index: number, item: IGuardian): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackAppointmentSlotById(index: number, item: IAppointmentSlot): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApplicant>>): void {
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

  protected updateForm(applicant: IApplicant): void {
    this.editForm.patchValue({
      id: applicant.id,
      surname: applicant.surname,
      otherNames: applicant.otherNames,
      maidenName: applicant.maidenName,
      changedName: applicant.changedName,
      gender: applicant.gender,
      maritalStatus: applicant.maritalStatus,
      dateOfBirth: applicant.dateOfBirth,
      idNumber: applicant.idNumber,
      birthEntryNumber: applicant.birthEntryNumber,
      democraphicDetails: applicant.democraphicDetails,
      declaration: applicant.declaration,
      guardian: applicant.guardian,
      user: applicant.user,
      appointmentSlot: applicant.appointmentSlot,
    });

    this.democraphicDetailsCollection = this.demographicDetailsService.addDemographicDetailsToCollectionIfMissing(
      this.democraphicDetailsCollection,
      applicant.democraphicDetails
    );
    this.declarationsCollection = this.declarationService.addDeclarationToCollectionIfMissing(
      this.declarationsCollection,
      applicant.declaration
    );
    this.guardiansCollection = this.guardianService.addGuardianToCollectionIfMissing(this.guardiansCollection, applicant.guardian);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, applicant.user);
    this.appointmentSlotsSharedCollection = this.appointmentSlotService.addAppointmentSlotToCollectionIfMissing(
      this.appointmentSlotsSharedCollection,
      applicant.appointmentSlot
    );
  }

  protected loadRelationshipsOptions(): void {
    this.demographicDetailsService
      .query({ filter: 'applicant-is-null' })
      .pipe(map((res: HttpResponse<IDemographicDetails[]>) => res.body ?? []))
      .pipe(
        map((demographicDetails: IDemographicDetails[]) =>
          this.demographicDetailsService.addDemographicDetailsToCollectionIfMissing(
            demographicDetails,
            this.editForm.get('democraphicDetails')!.value
          )
        )
      )
      .subscribe((demographicDetails: IDemographicDetails[]) => (this.democraphicDetailsCollection = demographicDetails));

    this.declarationService
      .query({ filter: 'applicant-is-null' })
      .pipe(map((res: HttpResponse<IDeclaration[]>) => res.body ?? []))
      .pipe(
        map((declarations: IDeclaration[]) =>
          this.declarationService.addDeclarationToCollectionIfMissing(declarations, this.editForm.get('declaration')!.value)
        )
      )
      .subscribe((declarations: IDeclaration[]) => (this.declarationsCollection = declarations));

    this.guardianService
      .query({ filter: 'applicant-is-null' })
      .pipe(map((res: HttpResponse<IGuardian[]>) => res.body ?? []))
      .pipe(
        map((guardians: IGuardian[]) =>
          this.guardianService.addGuardianToCollectionIfMissing(guardians, this.editForm.get('guardian')!.value)
        )
      )
      .subscribe((guardians: IGuardian[]) => (this.guardiansCollection = guardians));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.appointmentSlotService
      .query()
      .pipe(map((res: HttpResponse<IAppointmentSlot[]>) => res.body ?? []))
      .pipe(
        map((appointmentSlots: IAppointmentSlot[]) =>
          this.appointmentSlotService.addAppointmentSlotToCollectionIfMissing(appointmentSlots, this.editForm.get('appointmentSlot')!.value)
        )
      )
      .subscribe((appointmentSlots: IAppointmentSlot[]) => (this.appointmentSlotsSharedCollection = appointmentSlots));
  }

  protected createFromForm(): IApplicant {
    return {
      ...new Applicant(),
      id: this.editForm.get(['id'])!.value,
      surname: this.editForm.get(['surname'])!.value,
      otherNames: this.editForm.get(['otherNames'])!.value,
      maidenName: this.editForm.get(['maidenName'])!.value,
      changedName: this.editForm.get(['changedName'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      maritalStatus: this.editForm.get(['maritalStatus'])!.value,
      dateOfBirth: this.editForm.get(['dateOfBirth'])!.value,
      idNumber: this.editForm.get(['idNumber'])!.value,
      birthEntryNumber: this.editForm.get(['birthEntryNumber'])!.value,
      democraphicDetails: this.editForm.get(['democraphicDetails'])!.value,
      declaration: this.editForm.get(['declaration'])!.value,
      guardian: this.editForm.get(['guardian'])!.value,
      user: this.editForm.get(['user'])!.value,
      appointmentSlot: this.editForm.get(['appointmentSlot'])!.value,
    };
  }
}
