import * as dayjs from 'dayjs';
import { IApplicant } from 'app/entities/applicant/applicant.model';

export interface IMarriageDetails {
  id?: number;
  dateOfMarriage?: dayjs.Dayjs;
  spouseFullName?: string;
  placeOfMarriage?: string;
  spousePlaceOfBirth?: string;
  countryOfMarriage?: string;
  spouseCountryOfBirth?: string;
  marriageNumber?: string;
  marriedBefore?: boolean;
  marriageOrder?: string;
  devorceOrder?: string;
  previousSppouses?: string;
  applicant?: IApplicant;
}

export class MarriageDetails implements IMarriageDetails {
  constructor(
    public id?: number,
    public dateOfMarriage?: dayjs.Dayjs,
    public spouseFullName?: string,
    public placeOfMarriage?: string,
    public spousePlaceOfBirth?: string,
    public countryOfMarriage?: string,
    public spouseCountryOfBirth?: string,
    public marriageNumber?: string,
    public marriedBefore?: boolean,
    public marriageOrder?: string,
    public devorceOrder?: string,
    public previousSppouses?: string,
    public applicant?: IApplicant
  ) {
    this.marriedBefore = this.marriedBefore ?? false;
  }
}

export function getMarriageDetailsIdentifier(marriageDetails: IMarriageDetails): number | undefined {
  return marriageDetails.id;
}
