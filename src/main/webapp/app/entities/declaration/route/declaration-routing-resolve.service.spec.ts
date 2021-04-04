jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDeclaration, Declaration } from '../declaration.model';
import { DeclarationService } from '../service/declaration.service';

import { DeclarationRoutingResolveService } from './declaration-routing-resolve.service';

describe('Service Tests', () => {
  describe('Declaration routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DeclarationRoutingResolveService;
    let service: DeclarationService;
    let resultDeclaration: IDeclaration | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DeclarationRoutingResolveService);
      service = TestBed.inject(DeclarationService);
      resultDeclaration = undefined;
    });

    describe('resolve', () => {
      it('should return IDeclaration returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDeclaration = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDeclaration).toEqual({ id: 123 });
      });

      it('should return new IDeclaration if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDeclaration = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDeclaration).toEqual(new Declaration());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDeclaration = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDeclaration).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
