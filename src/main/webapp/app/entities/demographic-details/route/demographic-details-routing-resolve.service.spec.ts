jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDemographicDetails, DemographicDetails } from '../demographic-details.model';
import { DemographicDetailsService } from '../service/demographic-details.service';

import { DemographicDetailsRoutingResolveService } from './demographic-details-routing-resolve.service';

describe('Service Tests', () => {
  describe('DemographicDetails routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DemographicDetailsRoutingResolveService;
    let service: DemographicDetailsService;
    let resultDemographicDetails: IDemographicDetails | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DemographicDetailsRoutingResolveService);
      service = TestBed.inject(DemographicDetailsService);
      resultDemographicDetails = undefined;
    });

    describe('resolve', () => {
      it('should return IDemographicDetails returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDemographicDetails = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDemographicDetails).toEqual({ id: 123 });
      });

      it('should return new IDemographicDetails if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDemographicDetails = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDemographicDetails).toEqual(new DemographicDetails());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDemographicDetails = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDemographicDetails).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
