import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICountry, Country } from '../country.model';
import { CountryService } from '../service/country.service';
import { IApplicant } from 'app/entities/applicant/applicant.model';
import { ApplicantService } from 'app/entities/applicant/service/applicant.service';

@Component({
  selector: 'jhi-country-update',
  templateUrl: './country-update.component.html',
})
export class CountryUpdateComponent implements OnInit {
  isSaving = false;

  applicantsSharedCollection: IApplicant[] = [];

  editForm = this.fb.group({
    id: [],
    countryName: [null, [Validators.required]],
    countryCode: [null, [Validators.required]],
    callingCode: [null, [Validators.required]],
    subRegion: [],
    region: [],
    population: [],
    timeZone: [null, [Validators.maxLength(15)]],
    numericCode: [],
    applicant: [],
  });

  constructor(
    protected countryService: CountryService,
    protected applicantService: ApplicantService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ country }) => {
      this.updateForm(country);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const country = this.createFromForm();
    if (country.id !== undefined) {
      this.subscribeToSaveResponse(this.countryService.update(country));
    } else {
      this.subscribeToSaveResponse(this.countryService.create(country));
    }
  }

  trackApplicantById(index: number, item: IApplicant): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICountry>>): void {
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

  protected updateForm(country: ICountry): void {
    this.editForm.patchValue({
      id: country.id,
      countryName: country.countryName,
      countryCode: country.countryCode,
      callingCode: country.callingCode,
      subRegion: country.subRegion,
      region: country.region,
      population: country.population,
      timeZone: country.timeZone,
      numericCode: country.numericCode,
      applicant: country.applicant,
    });

    this.applicantsSharedCollection = this.applicantService.addApplicantToCollectionIfMissing(
      this.applicantsSharedCollection,
      country.applicant
    );
  }

  protected loadRelationshipsOptions(): void {
    this.applicantService
      .query()
      .pipe(map((res: HttpResponse<IApplicant[]>) => res.body ?? []))
      .pipe(
        map((applicants: IApplicant[]) =>
          this.applicantService.addApplicantToCollectionIfMissing(applicants, this.editForm.get('applicant')!.value)
        )
      )
      .subscribe((applicants: IApplicant[]) => (this.applicantsSharedCollection = applicants));
  }

  protected createFromForm(): ICountry {
    return {
      ...new Country(),
      id: this.editForm.get(['id'])!.value,
      countryName: this.editForm.get(['countryName'])!.value,
      countryCode: this.editForm.get(['countryCode'])!.value,
      callingCode: this.editForm.get(['callingCode'])!.value,
      subRegion: this.editForm.get(['subRegion'])!.value,
      region: this.editForm.get(['region'])!.value,
      population: this.editForm.get(['population'])!.value,
      timeZone: this.editForm.get(['timeZone'])!.value,
      numericCode: this.editForm.get(['numericCode'])!.value,
      applicant: this.editForm.get(['applicant'])!.value,
    };
  }
}
