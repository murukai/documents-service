import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { DemographicDetailsDetailComponent } from './demographic-details-detail.component';

describe('Component Tests', () => {
  describe('DemographicDetails Management Detail Component', () => {
    let comp: DemographicDetailsDetailComponent;
    let fixture: ComponentFixture<DemographicDetailsDetailComponent>;
    let dataUtils: DataUtils;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DemographicDetailsDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ demographicDetails: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DemographicDetailsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DemographicDetailsDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = TestBed.inject(DataUtils);
    });

    describe('OnInit', () => {
      it('Should load demographicDetails on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.demographicDetails).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeBase64, fakeContentType);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeBase64, fakeContentType);
      });
    });
  });
});
