import { IApplicant } from 'app/entities/applicant/applicant.model';
import { EyeColor } from 'app/entities/enumerations/eye-color.model';
import { HairColor } from 'app/entities/enumerations/hair-color.model';

export interface IDemographicDetails {
  id?: number;
  eyeColor?: EyeColor;
  hairColor?: HairColor;
  visibleMarks?: string;
  profession?: string;
  placeOfBirth?: string;
  imageContentType?: string | null;
  image?: string | null;
  applicant?: IApplicant | null;
}

export class DemographicDetails implements IDemographicDetails {
  constructor(
    public id?: number,
    public eyeColor?: EyeColor,
    public hairColor?: HairColor,
    public visibleMarks?: string,
    public profession?: string,
    public placeOfBirth?: string,
    public imageContentType?: string | null,
    public image?: string | null,
    public applicant?: IApplicant | null
  ) {}
}

export function getDemographicDetailsIdentifier(demographicDetails: IDemographicDetails): number | undefined {
  return demographicDetails.id;
}
