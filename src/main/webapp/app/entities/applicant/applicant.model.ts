import * as dayjs from 'dayjs';
import { IDemographicDetails } from 'app/entities/demographic-details/demographic-details.model';
import { IDeclaration } from 'app/entities/declaration/declaration.model';
import { IGuardian } from 'app/entities/guardian/guardian.model';
import { IAddress } from 'app/entities/address/address.model';
import { ICountry } from 'app/entities/country/country.model';
import { IUser } from 'app/entities/user/user.model';
import { IMarriageDetails } from 'app/entities/marriage-details/marriage-details.model';
import { INextOfKeen } from 'app/entities/next-of-keen/next-of-keen.model';
import { IAppointmentSlot } from 'app/entities/appointment-slot/appointment-slot.model';
import { Gender } from 'app/entities/enumerations/gender.model';
import { MaritalStatus } from 'app/entities/enumerations/marital-status.model';

export interface IApplicant {
  id?: number;
  surname?: string;
  otherNames?: string;
  maidenName?: string;
  changedName?: boolean;
  gender?: Gender | null;
  maritalStatus?: MaritalStatus | null;
  dateOfBirth?: dayjs.Dayjs;
  idNumber?: string;
  birthEntryNumber?: string;
  democraphicDetails?: IDemographicDetails;
  declaration?: IDeclaration | null;
  guardian?: IGuardian | null;
  addresses?: IAddress[] | null;
  countryOfBirths?: ICountry[] | null;
  user?: IUser;
  marriageDetails?: IMarriageDetails | null;
  nextOfKeen?: INextOfKeen | null;
  appointmentSlot?: IAppointmentSlot | null;
}

export class Applicant implements IApplicant {
  constructor(
    public id?: number,
    public surname?: string,
    public otherNames?: string,
    public maidenName?: string,
    public changedName?: boolean,
    public gender?: Gender | null,
    public maritalStatus?: MaritalStatus | null,
    public dateOfBirth?: dayjs.Dayjs,
    public idNumber?: string,
    public birthEntryNumber?: string,
    public democraphicDetails?: IDemographicDetails,
    public declaration?: IDeclaration | null,
    public guardian?: IGuardian | null,
    public addresses?: IAddress[] | null,
    public countryOfBirths?: ICountry[] | null,
    public user?: IUser,
    public marriageDetails?: IMarriageDetails | null,
    public nextOfKeen?: INextOfKeen | null,
    public appointmentSlot?: IAppointmentSlot | null
  ) {
    this.changedName = this.changedName ?? false;
  }
}

export function getApplicantIdentifier(applicant: IApplicant): number | undefined {
  return applicant.id;
}
