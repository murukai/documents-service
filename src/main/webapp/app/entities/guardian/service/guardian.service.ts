import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGuardian, getGuardianIdentifier } from '../guardian.model';

export type EntityResponseType = HttpResponse<IGuardian>;
export type EntityArrayResponseType = HttpResponse<IGuardian[]>;

@Injectable({ providedIn: 'root' })
export class GuardianService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/guardians');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(guardian: IGuardian): Observable<EntityResponseType> {
    return this.http.post<IGuardian>(this.resourceUrl, guardian, { observe: 'response' });
  }

  update(guardian: IGuardian): Observable<EntityResponseType> {
    return this.http.put<IGuardian>(`${this.resourceUrl}/${getGuardianIdentifier(guardian) as number}`, guardian, { observe: 'response' });
  }

  partialUpdate(guardian: IGuardian): Observable<EntityResponseType> {
    return this.http.patch<IGuardian>(`${this.resourceUrl}/${getGuardianIdentifier(guardian) as number}`, guardian, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGuardian>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGuardian[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addGuardianToCollectionIfMissing(guardianCollection: IGuardian[], ...guardiansToCheck: (IGuardian | null | undefined)[]): IGuardian[] {
    const guardians: IGuardian[] = guardiansToCheck.filter(isPresent);
    if (guardians.length > 0) {
      const guardianCollectionIdentifiers = guardianCollection.map(guardianItem => getGuardianIdentifier(guardianItem)!);
      const guardiansToAdd = guardians.filter(guardianItem => {
        const guardianIdentifier = getGuardianIdentifier(guardianItem);
        if (guardianIdentifier == null || guardianCollectionIdentifiers.includes(guardianIdentifier)) {
          return false;
        }
        guardianCollectionIdentifiers.push(guardianIdentifier);
        return true;
      });
      return [...guardiansToAdd, ...guardianCollection];
    }
    return guardianCollection;
  }
}
