jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MarriageDetailsService } from '../service/marriage-details.service';
import { IMarriageDetails, MarriageDetails } from '../marriage-details.model';
import { IApplicant } from 'app/entities/applicant/applicant.model';
import { ApplicantService } from 'app/entities/applicant/service/applicant.service';

import { MarriageDetailsUpdateComponent } from './marriage-details-update.component';

describe('Component Tests', () => {
  describe('MarriageDetails Management Update Component', () => {
    let comp: MarriageDetailsUpdateComponent;
    let fixture: ComponentFixture<MarriageDetailsUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let marriageDetailsService: MarriageDetailsService;
    let applicantService: ApplicantService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MarriageDetailsUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MarriageDetailsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MarriageDetailsUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      marriageDetailsService = TestBed.inject(MarriageDetailsService);
      applicantService = TestBed.inject(ApplicantService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call applicant query and add missing value', () => {
        const marriageDetails: IMarriageDetails = { id: 456 };
        const applicant: IApplicant = { id: 77942 };
        marriageDetails.applicant = applicant;

        const applicantCollection: IApplicant[] = [{ id: 19193 }];
        spyOn(applicantService, 'query').and.returnValue(of(new HttpResponse({ body: applicantCollection })));
        const expectedCollection: IApplicant[] = [applicant, ...applicantCollection];
        spyOn(applicantService, 'addApplicantToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ marriageDetails });
        comp.ngOnInit();

        expect(applicantService.query).toHaveBeenCalled();
        expect(applicantService.addApplicantToCollectionIfMissing).toHaveBeenCalledWith(applicantCollection, applicant);
        expect(comp.applicantsCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const marriageDetails: IMarriageDetails = { id: 456 };
        const applicant: IApplicant = { id: 1607 };
        marriageDetails.applicant = applicant;

        activatedRoute.data = of({ marriageDetails });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(marriageDetails));
        expect(comp.applicantsCollection).toContain(applicant);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const marriageDetails = { id: 123 };
        spyOn(marriageDetailsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ marriageDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: marriageDetails }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(marriageDetailsService.update).toHaveBeenCalledWith(marriageDetails);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const marriageDetails = new MarriageDetails();
        spyOn(marriageDetailsService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ marriageDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: marriageDetails }));
        saveSubject.complete();

        // THEN
        expect(marriageDetailsService.create).toHaveBeenCalledWith(marriageDetails);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const marriageDetails = { id: 123 };
        spyOn(marriageDetailsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ marriageDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(marriageDetailsService.update).toHaveBeenCalledWith(marriageDetails);
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
