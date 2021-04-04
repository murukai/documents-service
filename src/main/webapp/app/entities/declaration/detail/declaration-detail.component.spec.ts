import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DeclarationDetailComponent } from './declaration-detail.component';

describe('Component Tests', () => {
  describe('Declaration Management Detail Component', () => {
    let comp: DeclarationDetailComponent;
    let fixture: ComponentFixture<DeclarationDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DeclarationDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ declaration: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DeclarationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DeclarationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load declaration on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.declaration).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
