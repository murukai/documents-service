import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { CitizenOptions } from 'app/entities/enumerations/citizen-options.model';
import { PassportOptions } from 'app/entities/enumerations/passport-options.model';
import { ForeignDocumentsOptions } from 'app/entities/enumerations/foreign-documents-options.model';
import { IDeclaration, Declaration } from '../declaration.model';

import { DeclarationService } from './declaration.service';

describe('Service Tests', () => {
  describe('Declaration Service', () => {
    let service: DeclarationService;
    let httpMock: HttpTestingController;
    let elemDefault: IDeclaration;
    let expectedResult: IDeclaration | IDeclaration[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DeclarationService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        citizen: CitizenOptions.BIRTH,
        passport: PassportOptions.NO,
        foreignPassport: ForeignDocumentsOptions.NA,
        passportNumber: 'AAAAAAA',
        renouncedCitizenship: ForeignDocumentsOptions.NA,
        passportSurrendered: ForeignDocumentsOptions.NA,
        foreignPassportNumber: 'AAAAAAA',
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

      it('should create a Declaration', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Declaration()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Declaration', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            citizen: 'BBBBBB',
            passport: 'BBBBBB',
            foreignPassport: 'BBBBBB',
            passportNumber: 'BBBBBB',
            renouncedCitizenship: 'BBBBBB',
            passportSurrendered: 'BBBBBB',
            foreignPassportNumber: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Declaration', () => {
        const patchObject = Object.assign(
          {
            citizen: 'BBBBBB',
            passport: 'BBBBBB',
            foreignPassport: 'BBBBBB',
            foreignPassportNumber: 'BBBBBB',
          },
          new Declaration()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Declaration', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            citizen: 'BBBBBB',
            passport: 'BBBBBB',
            foreignPassport: 'BBBBBB',
            passportNumber: 'BBBBBB',
            renouncedCitizenship: 'BBBBBB',
            passportSurrendered: 'BBBBBB',
            foreignPassportNumber: 'BBBBBB',
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

      it('should delete a Declaration', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDeclarationToCollectionIfMissing', () => {
        it('should add a Declaration to an empty array', () => {
          const declaration: IDeclaration = { id: 123 };
          expectedResult = service.addDeclarationToCollectionIfMissing([], declaration);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(declaration);
        });

        it('should not add a Declaration to an array that contains it', () => {
          const declaration: IDeclaration = { id: 123 };
          const declarationCollection: IDeclaration[] = [
            {
              ...declaration,
            },
            { id: 456 },
          ];
          expectedResult = service.addDeclarationToCollectionIfMissing(declarationCollection, declaration);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Declaration to an array that doesn't contain it", () => {
          const declaration: IDeclaration = { id: 123 };
          const declarationCollection: IDeclaration[] = [{ id: 456 }];
          expectedResult = service.addDeclarationToCollectionIfMissing(declarationCollection, declaration);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(declaration);
        });

        it('should add only unique Declaration to an array', () => {
          const declarationArray: IDeclaration[] = [{ id: 123 }, { id: 456 }, { id: 29632 }];
          const declarationCollection: IDeclaration[] = [{ id: 123 }];
          expectedResult = service.addDeclarationToCollectionIfMissing(declarationCollection, ...declarationArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const declaration: IDeclaration = { id: 123 };
          const declaration2: IDeclaration = { id: 456 };
          expectedResult = service.addDeclarationToCollectionIfMissing([], declaration, declaration2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(declaration);
          expect(expectedResult).toContain(declaration2);
        });

        it('should accept null and undefined values', () => {
          const declaration: IDeclaration = { id: 123 };
          expectedResult = service.addDeclarationToCollectionIfMissing([], null, declaration, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(declaration);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
