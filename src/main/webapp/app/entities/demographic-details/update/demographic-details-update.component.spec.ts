jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DemographicDetailsService } from '../service/demographic-details.service';
import { IDemographicDetails, DemographicDetails } from '../demographic-details.model';

import { DemographicDetailsUpdateComponent } from './demographic-details-update.component';

describe('Component Tests', () => {
  describe('DemographicDetails Management Update Component', () => {
    let comp: DemographicDetailsUpdateComponent;
    let fixture: ComponentFixture<DemographicDetailsUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let demographicDetailsService: DemographicDetailsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DemographicDetailsUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DemographicDetailsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DemographicDetailsUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      demographicDetailsService = TestBed.inject(DemographicDetailsService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const demographicDetails: IDemographicDetails = { id: 456 };

        activatedRoute.data = of({ demographicDetails });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(demographicDetails));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const demographicDetails = { id: 123 };
        spyOn(demographicDetailsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ demographicDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: demographicDetails }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(demographicDetailsService.update).toHaveBeenCalledWith(demographicDetails);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const demographicDetails = new DemographicDetails();
        spyOn(demographicDetailsService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ demographicDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: demographicDetails }));
        saveSubject.complete();

        // THEN
        expect(demographicDetailsService.create).toHaveBeenCalledWith(demographicDetails);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const demographicDetails = { id: 123 };
        spyOn(demographicDetailsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ demographicDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(demographicDetailsService.update).toHaveBeenCalledWith(demographicDetails);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
