import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDemographicDetails, getDemographicDetailsIdentifier } from '../demographic-details.model';

export type EntityResponseType = HttpResponse<IDemographicDetails>;
export type EntityArrayResponseType = HttpResponse<IDemographicDetails[]>;

@Injectable({ providedIn: 'root' })
export class DemographicDetailsService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/demographic-details');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(demographicDetails: IDemographicDetails): Observable<EntityResponseType> {
    return this.http.post<IDemographicDetails>(this.resourceUrl, demographicDetails, { observe: 'response' });
  }

  update(demographicDetails: IDemographicDetails): Observable<EntityResponseType> {
    return this.http.put<IDemographicDetails>(
      `${this.resourceUrl}/${getDemographicDetailsIdentifier(demographicDetails) as number}`,
      demographicDetails,
      { observe: 'response' }
    );
  }

  partialUpdate(demographicDetails: IDemographicDetails): Observable<EntityResponseType> {
    return this.http.patch<IDemographicDetails>(
      `${this.resourceUrl}/${getDemographicDetailsIdentifier(demographicDetails) as number}`,
      demographicDetails,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDemographicDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDemographicDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDemographicDetailsToCollectionIfMissing(
    demographicDetailsCollection: IDemographicDetails[],
    ...demographicDetailsToCheck: (IDemographicDetails | null | undefined)[]
  ): IDemographicDetails[] {
    const demographicDetails: IDemographicDetails[] = demographicDetailsToCheck.filter(isPresent);
    if (demographicDetails.length > 0) {
      const demographicDetailsCollectionIdentifiers = demographicDetailsCollection.map(
        demographicDetailsItem => getDemographicDetailsIdentifier(demographicDetailsItem)!
      );
      const demographicDetailsToAdd = demographicDetails.filter(demographicDetailsItem => {
        const demographicDetailsIdentifier = getDemographicDetailsIdentifier(demographicDetailsItem);
        if (demographicDetailsIdentifier == null || demographicDetailsCollectionIdentifiers.includes(demographicDetailsIdentifier)) {
          return false;
        }
        demographicDetailsCollectionIdentifiers.push(demographicDetailsIdentifier);
        return true;
      });
      return [...demographicDetailsToAdd, ...demographicDetailsCollection];
    }
    return demographicDetailsCollection;
  }
}
