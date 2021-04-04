jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ApplicantService } from '../service/applicant.service';
import { IApplicant, Applicant } from '../applicant.model';
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

import { ApplicantUpdateComponent } from './applicant-update.component';

describe('Component Tests', () => {
  describe('Applicant Management Update Component', () => {
    let comp: ApplicantUpdateComponent;
    let fixture: ComponentFixture<ApplicantUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let applicantService: ApplicantService;
    let demographicDetailsService: DemographicDetailsService;
    let declarationService: DeclarationService;
    let guardianService: GuardianService;
    let userService: UserService;
    let appointmentSlotService: AppointmentSlotService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ApplicantUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ApplicantUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ApplicantUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      applicantService = TestBed.inject(ApplicantService);
      demographicDetailsService = TestBed.inject(DemographicDetailsService);
      declarationService = TestBed.inject(DeclarationService);
      guardianService = TestBed.inject(GuardianService);
      userService = TestBed.inject(UserService);
      appointmentSlotService = TestBed.inject(AppointmentSlotService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call democraphicDetails query and add missing value', () => {
        const applicant: IApplicant = { id: 456 };
        const democraphicDetails: IDemographicDetails = { id: 99056 };
        applicant.democraphicDetails = democraphicDetails;

        const democraphicDetailsCollection: IDemographicDetails[] = [{ id: 78042 }];
        spyOn(demographicDetailsService, 'query').and.returnValue(of(new HttpResponse({ body: democraphicDetailsCollection })));
        const expectedCollection: IDemographicDetails[] = [democraphicDetails, ...democraphicDetailsCollection];
        spyOn(demographicDetailsService, 'addDemographicDetailsToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ applicant });
        comp.ngOnInit();

        expect(demographicDetailsService.query).toHaveBeenCalled();
        expect(demographicDetailsService.addDemographicDetailsToCollectionIfMissing).toHaveBeenCalledWith(
          democraphicDetailsCollection,
          democraphicDetails
        );
        expect(comp.democraphicDetailsCollection).toEqual(expectedCollection);
      });

      it('Should call declaration query and add missing value', () => {
        const applicant: IApplicant = { id: 456 };
        const declaration: IDeclaration = { id: 47264 };
        applicant.declaration = declaration;

        const declarationCollection: IDeclaration[] = [{ id: 8994 }];
        spyOn(declarationService, 'query').and.returnValue(of(new HttpResponse({ body: declarationCollection })));
        const expectedCollection: IDeclaration[] = [declaration, ...declarationCollection];
        spyOn(declarationService, 'addDeclarationToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ applicant });
        comp.ngOnInit();

        expect(declarationService.query).toHaveBeenCalled();
        expect(declarationService.addDeclarationToCollectionIfMissing).toHaveBeenCalledWith(declarationCollection, declaration);
        expect(comp.declarationsCollection).toEqual(expectedCollection);
      });

      it('Should call guardian query and add missing value', () => {
        const applicant: IApplicant = { id: 456 };
        const guardian: IGuardian = { id: 27213 };
        applicant.guardian = guardian;

        const guardianCollection: IGuardian[] = [{ id: 63569 }];
        spyOn(guardianService, 'query').and.returnValue(of(new HttpResponse({ body: guardianCollection })));
        const expectedCollection: IGuardian[] = [guardian, ...guardianCollection];
        spyOn(guardianService, 'addGuardianToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ applicant });
        comp.ngOnInit();

        expect(guardianService.query).toHaveBeenCalled();
        expect(guardianService.addGuardianToCollectionIfMissing).toHaveBeenCalledWith(guardianCollection, guardian);
        expect(comp.guardiansCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const applicant: IApplicant = { id: 456 };
        const user: IUser = { id: 56837 };
        applicant.user = user;

        const userCollection: IUser[] = [{ id: 46183 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ applicant });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call AppointmentSlot query and add missing value', () => {
        const applicant: IApplicant = { id: 456 };
        const appointmentSlot: IAppointmentSlot = { id: 21029 };
        applicant.appointmentSlot = appointmentSlot;

        const appointmentSlotCollection: IAppointmentSlot[] = [{ id: 49393 }];
        spyOn(appointmentSlotService, 'query').and.returnValue(of(new HttpResponse({ body: appointmentSlotCollection })));
        const additionalAppointmentSlots = [appointmentSlot];
        const expectedCollection: IAppointmentSlot[] = [...additionalAppointmentSlots, ...appointmentSlotCollection];
        spyOn(appointmentSlotService, 'addAppointmentSlotToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ applicant });
        comp.ngOnInit();

        expect(appointmentSlotService.query).toHaveBeenCalled();
        expect(appointmentSlotService.addAppointmentSlotToCollectionIfMissing).toHaveBeenCalledWith(
          appointmentSlotCollection,
          ...additionalAppointmentSlots
        );
        expect(comp.appointmentSlotsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const applicant: IApplicant = { id: 456 };
        const democraphicDetails: IDemographicDetails = { id: 41962 };
        applicant.democraphicDetails = democraphicDetails;
        const declaration: IDeclaration = { id: 43303 };
        applicant.declaration = declaration;
        const guardian: IGuardian = { id: 77526 };
        applicant.guardian = guardian;
        const user: IUser = { id: 13444 };
        applicant.user = user;
        const appointmentSlot: IAppointmentSlot = { id: 57896 };
        applicant.appointmentSlot = appointmentSlot;

        activatedRoute.data = of({ applicant });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(applicant));
        expect(comp.democraphicDetailsCollection).toContain(democraphicDetails);
        expect(comp.declarationsCollection).toContain(declaration);
        expect(comp.guardiansCollection).toContain(guardian);
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.appointmentSlotsSharedCollection).toContain(appointmentSlot);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const applicant = { id: 123 };
        spyOn(applicantService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ applicant });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: applicant }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(applicantService.update).toHaveBeenCalledWith(applicant);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const applicant = new Applicant();
        spyOn(applicantService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ applicant });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: applicant }));
        saveSubject.complete();

        // THEN
        expect(applicantService.create).toHaveBeenCalledWith(applicant);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const applicant = { id: 123 };
        spyOn(applicantService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ applicant });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(applicantService.update).toHaveBeenCalledWith(applicant);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackDemographicDetailsById', () => {
        it('Should return tracked DemographicDetails primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDemographicDetailsById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackDeclarationById', () => {
        it('Should return tracked Declaration primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDeclarationById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackGuardianById', () => {
        it('Should return tracked Guardian primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackGuardianById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackAppointmentSlotById', () => {
        it('Should return tracked AppointmentSlot primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAppointmentSlotById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
