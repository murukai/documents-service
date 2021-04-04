import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGuardian, Guardian } from '../guardian.model';

import { GuardianService } from './guardian.service';

describe('Service Tests', () => {
  describe('Guardian Service', () => {
    let service: GuardianService;
    let httpMock: HttpTestingController;
    let elemDefault: IGuardian;
    let expectedResult: IGuardian | IGuardian[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(GuardianService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        fullName: 'AAAAAAA',
        idNumber: 'AAAAAAA',
        relationshipToApplicant: 'AAAAAAA',
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

      it('should create a Guardian', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Guardian()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Guardian', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            fullName: 'BBBBBB',
            idNumber: 'BBBBBB',
            relationshipToApplicant: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Guardian', () => {
        const patchObject = Object.assign(
          {
            fullName: 'BBBBBB',
          },
          new Guardian()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Guardian', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            fullName: 'BBBBBB',
            idNumber: 'BBBBBB',
            relationshipToApplicant: 'BBBBBB',
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

      it('should delete a Guardian', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addGuardianToCollectionIfMissing', () => {
        it('should add a Guardian to an empty array', () => {
          const guardian: IGuardian = { id: 123 };
          expectedResult = service.addGuardianToCollectionIfMissing([], guardian);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(guardian);
        });

        it('should not add a Guardian to an array that contains it', () => {
          const guardian: IGuardian = { id: 123 };
          const guardianCollection: IGuardian[] = [
            {
              ...guardian,
            },
            { id: 456 },
          ];
          expectedResult = service.addGuardianToCollectionIfMissing(guardianCollection, guardian);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Guardian to an array that doesn't contain it", () => {
          const guardian: IGuardian = { id: 123 };
          const guardianCollection: IGuardian[] = [{ id: 456 }];
          expectedResult = service.addGuardianToCollectionIfMissing(guardianCollection, guardian);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(guardian);
        });

        it('should add only unique Guardian to an array', () => {
          const guardianArray: IGuardian[] = [{ id: 123 }, { id: 456 }, { id: 13229 }];
          const guardianCollection: IGuardian[] = [{ id: 123 }];
          expectedResult = service.addGuardianToCollectionIfMissing(guardianCollection, ...guardianArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const guardian: IGuardian = { id: 123 };
          const guardian2: IGuardian = { id: 456 };
          expectedResult = service.addGuardianToCollectionIfMissing([], guardian, guardian2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(guardian);
          expect(expectedResult).toContain(guardian2);
        });

        it('should accept null and undefined values', () => {
          const guardian: IGuardian = { id: 123 };
          expectedResult = service.addGuardianToCollectionIfMissing([], null, guardian, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(guardian);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
