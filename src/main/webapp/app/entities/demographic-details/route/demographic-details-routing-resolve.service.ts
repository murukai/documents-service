import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDemographicDetails, DemographicDetails } from '../demographic-details.model';
import { DemographicDetailsService } from '../service/demographic-details.service';

@Injectable({ providedIn: 'root' })
export class DemographicDetailsRoutingResolveService implements Resolve<IDemographicDetails> {
  constructor(protected service: DemographicDetailsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDemographicDetails> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((demographicDetails: HttpResponse<DemographicDetails>) => {
          if (demographicDetails.body) {
            return of(demographicDetails.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DemographicDetails());
  }
}
