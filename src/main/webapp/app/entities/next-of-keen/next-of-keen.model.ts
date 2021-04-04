import { IApplicant } from 'app/entities/applicant/applicant.model';
import { IAddress } from 'app/entities/address/address.model';

export interface INextOfKeen {
  id?: number;
  fullName?: string;
  relationshipToApplicant?: string;
  applicant?: IApplicant;
  address?: IAddress;
}

export class NextOfKeen implements INextOfKeen {
  constructor(
    public id?: number,
    public fullName?: string,
    public relationshipToApplicant?: string,
    public applicant?: IApplicant,
    public address?: IAddress
  ) {}
}

export function getNextOfKeenIdentifier(nextOfKeen: INextOfKeen): number | undefined {
  return nextOfKeen.id;
}
