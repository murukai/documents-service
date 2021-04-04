import { IApplicant } from 'app/entities/applicant/applicant.model';
import { CitizenOptions } from 'app/entities/enumerations/citizen-options.model';
import { PassportOptions } from 'app/entities/enumerations/passport-options.model';
import { ForeignDocumentsOptions } from 'app/entities/enumerations/foreign-documents-options.model';

export interface IDeclaration {
  id?: number;
  citizen?: CitizenOptions;
  passport?: PassportOptions;
  foreignPassport?: ForeignDocumentsOptions;
  passportNumber?: string;
  renouncedCitizenship?: ForeignDocumentsOptions;
  passportSurrendered?: ForeignDocumentsOptions;
  foreignPassportNumber?: string;
  applicant?: IApplicant | null;
}

export class Declaration implements IDeclaration {
  constructor(
    public id?: number,
    public citizen?: CitizenOptions,
    public passport?: PassportOptions,
    public foreignPassport?: ForeignDocumentsOptions,
    public passportNumber?: string,
    public renouncedCitizenship?: ForeignDocumentsOptions,
    public passportSurrendered?: ForeignDocumentsOptions,
    public foreignPassportNumber?: string,
    public applicant?: IApplicant | null
  ) {}
}

export function getDeclarationIdentifier(declaration: IDeclaration): number | undefined {
  return declaration.id;
}
