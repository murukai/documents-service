jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PassportService } from '../service/passport.service';
import { IPassport, Passport } from '../passport.model';

import { PassportUpdateComponent } from './passport-update.component';

describe('Component Tests', () => {
  describe('Passport Management Update Component', () => {
    let comp: PassportUpdateComponent;
    let fixture: ComponentFixture<PassportUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let passportService: PassportService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PassportUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PassportUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PassportUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      passportService = TestBed.inject(PassportService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const passport: IPassport = { id: 456 };

        activatedRoute.data = of({ passport });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(passport));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const passport = { id: 123 };
        spyOn(passportService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ passport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: passport }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(passportService.update).toHaveBeenCalledWith(passport);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const passport = new Passport();
        spyOn(passportService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ passport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: passport }));
        saveSubject.complete();

        // THEN
        expect(passportService.create).toHaveBeenCalledWith(passport);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const passport = { id: 123 };
        spyOn(passportService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ passport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(passportService.update).toHaveBeenCalledWith(passport);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
