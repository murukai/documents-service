import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDeclaration, getDeclarationIdentifier } from '../declaration.model';

export type EntityResponseType = HttpResponse<IDeclaration>;
export type EntityArrayResponseType = HttpResponse<IDeclaration[]>;

@Injectable({ providedIn: 'root' })
export class DeclarationService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/declarations');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(declaration: IDeclaration): Observable<EntityResponseType> {
    return this.http.post<IDeclaration>(this.resourceUrl, declaration, { observe: 'response' });
  }

  update(declaration: IDeclaration): Observable<EntityResponseType> {
    return this.http.put<IDeclaration>(`${this.resourceUrl}/${getDeclarationIdentifier(declaration) as number}`, declaration, {
      observe: 'response',
    });
  }

  partialUpdate(declaration: IDeclaration): Observable<EntityResponseType> {
    return this.http.patch<IDeclaration>(`${this.resourceUrl}/${getDeclarationIdentifier(declaration) as number}`, declaration, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDeclaration>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDeclaration[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDeclarationToCollectionIfMissing(
    declarationCollection: IDeclaration[],
    ...declarationsToCheck: (IDeclaration | null | undefined)[]
  ): IDeclaration[] {
    const declarations: IDeclaration[] = declarationsToCheck.filter(isPresent);
    if (declarations.length > 0) {
      const declarationCollectionIdentifiers = declarationCollection.map(declarationItem => getDeclarationIdentifier(declarationItem)!);
      const declarationsToAdd = declarations.filter(declarationItem => {
        const declarationIdentifier = getDeclarationIdentifier(declarationItem);
        if (declarationIdentifier == null || declarationCollectionIdentifiers.includes(declarationIdentifier)) {
          return false;
        }
        declarationCollectionIdentifiers.push(declarationIdentifier);
        return true;
      });
      return [...declarationsToAdd, ...declarationCollection];
    }
    return declarationCollection;
  }
}
