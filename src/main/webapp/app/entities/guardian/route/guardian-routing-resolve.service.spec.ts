jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IGuardian, Guardian } from '../guardian.model';
import { GuardianService } from '../service/guardian.service';

import { GuardianRoutingResolveService } from './guardian-routing-resolve.service';

describe('Service Tests', () => {
  describe('Guardian routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: GuardianRoutingResolveService;
    let service: GuardianService;
    let resultGuardian: IGuardian | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(GuardianRoutingResolveService);
      service = TestBed.inject(GuardianService);
      resultGuardian = undefined;
    });

    describe('resolve', () => {
      it('should return IGuardian returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultGuardian = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultGuardian).toEqual({ id: 123 });
      });

      it('should return new IGuardian if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultGuardian = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultGuardian).toEqual(new Guardian());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultGuardian = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultGuardian).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
