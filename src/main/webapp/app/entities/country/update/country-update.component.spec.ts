jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CountryService } from '../service/country.service';
import { ICountry, Country } from '../country.model';
import { IApplicant } from 'app/entities/applicant/applicant.model';
import { ApplicantService } from 'app/entities/applicant/service/applicant.service';

import { CountryUpdateComponent } from './country-update.component';

describe('Component Tests', () => {
  describe('Country Management Update Component', () => {
    let comp: CountryUpdateComponent;
    let fixture: ComponentFixture<CountryUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let countryService: CountryService;
    let applicantService: ApplicantService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CountryUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CountryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CountryUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      countryService = TestBed.inject(CountryService);
      applicantService = TestBed.inject(ApplicantService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Applicant query and add missing value', () => {
        const country: ICountry = { id: 456 };
        const applicant: IApplicant = { id: 45499 };
        country.applicant = applicant;

        const applicantCollection: IApplicant[] = [{ id: 33561 }];
        spyOn(applicantService, 'query').and.returnValue(of(new HttpResponse({ body: applicantCollection })));
        const additionalApplicants = [applicant];
        const expectedCollection: IApplicant[] = [...additionalApplicants, ...applicantCollection];
        spyOn(applicantService, 'addApplicantToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ country });
        comp.ngOnInit();

        expect(applicantService.query).toHaveBeenCalled();
        expect(applicantService.addApplicantToCollectionIfMissing).toHaveBeenCalledWith(applicantCollection, ...additionalApplicants);
        expect(comp.applicantsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const country: ICountry = { id: 456 };
        const applicant: IApplicant = { id: 49401 };
        country.applicant = applicant;

        activatedRoute.data = of({ country });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(country));
        expect(comp.applicantsSharedCollection).toContain(applicant);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const country = { id: 123 };
        spyOn(countryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ country });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: country }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(countryService.update).toHaveBeenCalledWith(country);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const country = new Country();
        spyOn(countryService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ country });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: country }));
        saveSubject.complete();

        // THEN
        expect(countryService.create).toHaveBeenCalledWith(country);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const country = { id: 123 };
        spyOn(countryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ country });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(countryService.update).toHaveBeenCalledWith(country);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackApplicantById', () => {
        it('Should return tracked Applicant primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackApplicantById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
