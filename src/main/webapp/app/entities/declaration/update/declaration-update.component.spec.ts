jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DeclarationService } from '../service/declaration.service';
import { IDeclaration, Declaration } from '../declaration.model';

import { DeclarationUpdateComponent } from './declaration-update.component';

describe('Component Tests', () => {
  describe('Declaration Management Update Component', () => {
    let comp: DeclarationUpdateComponent;
    let fixture: ComponentFixture<DeclarationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let declarationService: DeclarationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DeclarationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DeclarationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DeclarationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      declarationService = TestBed.inject(DeclarationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const declaration: IDeclaration = { id: 456 };

        activatedRoute.data = of({ declaration });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(declaration));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const declaration = { id: 123 };
        spyOn(declarationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ declaration });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: declaration }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(declarationService.update).toHaveBeenCalledWith(declaration);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const declaration = new Declaration();
        spyOn(declarationService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ declaration });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: declaration }));
        saveSubject.complete();

        // THEN
        expect(declarationService.create).toHaveBeenCalledWith(declaration);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const declaration = { id: 123 };
        spyOn(declarationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ declaration });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(declarationService.update).toHaveBeenCalledWith(declaration);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
