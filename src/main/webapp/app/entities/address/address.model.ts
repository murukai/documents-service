import { ICountry } from 'app/entities/country/country.model';
import { INextOfKeen } from 'app/entities/next-of-keen/next-of-keen.model';
import { IApplicant } from 'app/entities/applicant/applicant.model';

export interface IAddress {
  id?: number;
  streetAddress?: string;
  postalCode?: string;
  city?: string;
  stateProvince?: string | null;
  placeOfBirth?: string;
  telephone?: string;
  country?: ICountry;
  nextOfKeen?: INextOfKeen | null;
  applicant?: IApplicant | null;
}

export class Address implements IAddress {
  constructor(
    public id?: number,
    public streetAddress?: string,
    public postalCode?: string,
    public city?: string,
    public stateProvince?: string | null,
    public placeOfBirth?: string,
    public telephone?: string,
    public country?: ICountry,
    public nextOfKeen?: INextOfKeen | null,
    public applicant?: IApplicant | null
  ) {}
}

export function getAddressIdentifier(address: IAddress): number | undefined {
  return address.id;
}
