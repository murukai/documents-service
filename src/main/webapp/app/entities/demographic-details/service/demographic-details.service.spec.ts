import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { EyeColor } from 'app/entities/enumerations/eye-color.model';
import { HairColor } from 'app/entities/enumerations/hair-color.model';
import { IDemographicDetails, DemographicDetails } from '../demographic-details.model';

import { DemographicDetailsService } from './demographic-details.service';

describe('Service Tests', () => {
  describe('DemographicDetails Service', () => {
    let service: DemographicDetailsService;
    let httpMock: HttpTestingController;
    let elemDefault: IDemographicDetails;
    let expectedResult: IDemographicDetails | IDemographicDetails[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DemographicDetailsService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        eyeColor: EyeColor.BROWN,
        hairColor: HairColor.BLACK,
        visibleMarks: 'AAAAAAA',
        profession: 'AAAAAAA',
        placeOfBirth: 'AAAAAAA',
        imageContentType: 'image/png',
        image: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a DemographicDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new DemographicDetails()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DemographicDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            eyeColor: 'BBBBBB',
            hairColor: 'BBBBBB',
            visibleMarks: 'BBBBBB',
            profession: 'BBBBBB',
            placeOfBirth: 'BBBBBB',
            image: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a DemographicDetails', () => {
        const patchObject = Object.assign(
          {
            eyeColor: 'BBBBBB',
            hairColor: 'BBBBBB',
            visibleMarks: 'BBBBBB',
            placeOfBirth: 'BBBBBB',
          },
          new DemographicDetails()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of DemographicDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            eyeColor: 'BBBBBB',
            hairColor: 'BBBBBB',
            visibleMarks: 'BBBBBB',
            profession: 'BBBBBB',
            placeOfBirth: 'BBBBBB',
            image: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a DemographicDetails', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDemographicDetailsToCollectionIfMissing', () => {
        it('should add a DemographicDetails to an empty array', () => {
          const demographicDetails: IDemographicDetails = { id: 123 };
          expectedResult = service.addDemographicDetailsToCollectionIfMissing([], demographicDetails);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(demographicDetails);
        });

        it('should not add a DemographicDetails to an array that contains it', () => {
          const demographicDetails: IDemographicDetails = { id: 123 };
          const demographicDetailsCollection: IDemographicDetails[] = [
            {
              ...demographicDetails,
            },
            { id: 456 },
          ];
          expectedResult = service.addDemographicDetailsToCollectionIfMissing(demographicDetailsCollection, demographicDetails);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DemographicDetails to an array that doesn't contain it", () => {
          const demographicDetails: IDemographicDetails = { id: 123 };
          const demographicDetailsCollection: IDemographicDetails[] = [{ id: 456 }];
          expectedResult = service.addDemographicDetailsToCollectionIfMissing(demographicDetailsCollection, demographicDetails);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(demographicDetails);
        });

        it('should add only unique DemographicDetails to an array', () => {
          const demographicDetailsArray: IDemographicDetails[] = [{ id: 123 }, { id: 456 }, { id: 57034 }];
          const demographicDetailsCollection: IDemographicDetails[] = [{ id: 123 }];
          expectedResult = service.addDemographicDetailsToCollectionIfMissing(demographicDetailsCollection, ...demographicDetailsArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const demographicDetails: IDemographicDetails = { id: 123 };
          const demographicDetails2: IDemographicDetails = { id: 456 };
          expectedResult = service.addDemographicDetailsToCollectionIfMissing([], demographicDetails, demographicDetails2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(demographicDetails);
          expect(expectedResult).toContain(demographicDetails2);
        });

        it('should accept null and undefined values', () => {
          const demographicDetails: IDemographicDetails = { id: 123 };
          expectedResult = service.addDemographicDetailsToCollectionIfMissing([], null, demographicDetails, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(demographicDetails);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
