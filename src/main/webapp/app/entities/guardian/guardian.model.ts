import { IApplicant } from 'app/entities/applicant/applicant.model';

export interface IGuardian {
  id?: number;
  fullName?: string;
  idNumber?: string;
  relationshipToApplicant?: string;
  applicant?: IApplicant | null;
}

export class Guardian implements IGuardian {
  constructor(
    public id?: number,
    public fullName?: string,
    public idNumber?: string,
    public relationshipToApplicant?: string,
    public applicant?: IApplicant | null
  ) {}
}

export function getGuardianIdentifier(guardian: IGuardian): number | undefined {
  return guardian.id;
}
