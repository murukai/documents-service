jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { GuardianService } from '../service/guardian.service';
import { IGuardian, Guardian } from '../guardian.model';

import { GuardianUpdateComponent } from './guardian-update.component';

describe('Component Tests', () => {
  describe('Guardian Management Update Component', () => {
    let comp: GuardianUpdateComponent;
    let fixture: ComponentFixture<GuardianUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let guardianService: GuardianService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [GuardianUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(GuardianUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GuardianUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      guardianService = TestBed.inject(GuardianService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const guardian: IGuardian = { id: 456 };

        activatedRoute.data = of({ guardian });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(guardian));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const guardian = { id: 123 };
        spyOn(guardianService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ guardian });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: guardian }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(guardianService.update).toHaveBeenCalledWith(guardian);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const guardian = new Guardian();
        spyOn(guardianService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ guardian });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: guardian }));
        saveSubject.complete();

        // THEN
        expect(guardianService.create).toHaveBeenCalledWith(guardian);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const guardian = { id: 123 };
        spyOn(guardianService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ guardian });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(guardianService.update).toHaveBeenCalledWith(guardian);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
