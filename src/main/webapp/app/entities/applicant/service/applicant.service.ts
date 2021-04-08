import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApplicant, getApplicantIdentifier } from '../applicant.model';
import { INextOfKeen } from 'app/entities/next-of-keen/next-of-keen.model';
import { IMarriageDetails } from 'app/entities/marriage-details/marriage-details.model';
import { IGuardian } from 'app/entities/guardian/guardian.model';
import { IDemographicDetails } from 'app/entities/demographic-details/demographic-details.model';
import { IDeclaration } from 'app/entities/declaration/declaration.model';

export type EntityResponseType = HttpResponse<IApplicant>;
export type EntityArrayResponseType = HttpResponse<IApplicant[]>;

@Injectable({ providedIn: 'root' })
export class ApplicantService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/applicants');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(applicant: IApplicant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(applicant);
    return this.http
      .post<IApplicant>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(applicant: IApplicant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(applicant);
    return this.http
      .put<IApplicant>(`${this.resourceUrl}/${getApplicantIdentifier(applicant) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(applicant: IApplicant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(applicant);
    return this.http
      .patch<IApplicant>(`${this.resourceUrl}/${getApplicantIdentifier(applicant) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IApplicant>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  // added endpoints to query the database on the values that make up an applicant
  findNextOfKeen(id: number): Observable<EntityResponseType> {
    return this.http.get<INextOfKeen>(`${this.resourceUrl}/${id}/next-of-keen`, { observe: 'response' });
  }

  findMarriageDetails(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMarriageDetails>(`${this.resourceUrl}/${id}/marriage-details`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findGuardian(id: number): Observable<EntityResponseType> {
    return this.http.get<IGuardian>(`${this.resourceUrl}/${id}/guardian`, { observe: 'response' });
  }

  findDemographicDetails(id: number): Observable<EntityResponseType> {
    return this.http.get<IDemographicDetails>(`${this.resourceUrl}/${id}/demographic-details`, { observe: 'response' });
  }

  findDeclaration(id: number): Observable<EntityResponseType> {
    return this.http.get<IDeclaration>(`${this.resourceUrl}/${id}/declaration`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IApplicant[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addApplicantToCollectionIfMissing(
    applicantCollection: IApplicant[],
    ...applicantsToCheck: (IApplicant | null | undefined)[]
  ): IApplicant[] {
    const applicants: IApplicant[] = applicantsToCheck.filter(isPresent);
    if (applicants.length > 0) {
      const applicantCollectionIdentifiers = applicantCollection.map(applicantItem => getApplicantIdentifier(applicantItem)!);
      const applicantsToAdd = applicants.filter(applicantItem => {
        const applicantIdentifier = getApplicantIdentifier(applicantItem);
        if (applicantIdentifier == null || applicantCollectionIdentifiers.includes(applicantIdentifier)) {
          return false;
        }
        applicantCollectionIdentifiers.push(applicantIdentifier);
        return true;
      });
      return [...applicantsToAdd, ...applicantCollection];
    }
    return applicantCollection;
  }

  protected convertDateFromClient(applicant: IApplicant): IApplicant {
    return Object.assign({}, applicant, {
      dateOfBirth: applicant.dateOfBirth?.isValid() ? applicant.dateOfBirth.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateOfBirth = res.body.dateOfBirth ? dayjs(res.body.dateOfBirth) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((applicant: IApplicant) => {
        applicant.dateOfBirth = applicant.dateOfBirth ? dayjs(applicant.dateOfBirth) : undefined;
      });
    }
    return res;
  }
}
