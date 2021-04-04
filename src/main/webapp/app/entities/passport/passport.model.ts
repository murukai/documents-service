import * as dayjs from 'dayjs';

export interface IPassport {
  id?: number;
  passportNumber?: string;
  issuedAt?: string;
  issuedDate?: dayjs.Dayjs;
  lossDescription?: string;
}

export class Passport implements IPassport {
  constructor(
    public id?: number,
    public passportNumber?: string,
    public issuedAt?: string,
    public issuedDate?: dayjs.Dayjs,
    public lossDescription?: string
  ) {}
}

export function getPassportIdentifier(passport: IPassport): number | undefined {
  return passport.id;
}
