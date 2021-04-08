import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IAppointmentSlot } from 'app/entities/appointment-slot/appointment-slot.model';
import { IDeclaration } from 'app/entities/declaration/declaration.model';
import { IDemographicDetails } from 'app/entities/demographic-details/demographic-details.model';
import { IGuardian } from 'app/entities/guardian/guardian.model';
import { IMarriageDetails } from 'app/entities/marriage-details/marriage-details.model';
import { INextOfKeen } from 'app/entities/next-of-keen/next-of-keen.model';

import { IApplicant } from '../applicant.model';
import { ApplicantService } from 'app/entities/applicant/service/applicant.service';
import { MaritalStatus } from 'app/entities/enumerations/marital-status.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-applicant-detail',
  templateUrl: './applicant-detail.component.html',
  styleUrls: ['./applicant-detail.component.css'],
})
export class ApplicantDetailComponent implements OnInit {
  applicant: IApplicant | null = null;
  demographicDetails: IDemographicDetails | null = null;
  declaration: IDeclaration | null = null;
  marriageDetails: IMarriageDetails | null = null;
  nextOfKeen: INextOfKeen | null = null;
  appointmentSlot: IAppointmentSlot | null = null;
  guardian: IGuardian | null = null;

  constructor(protected activatedRoute: ActivatedRoute, protected applicantService: ApplicantService, protected dataUtils: DataUtils) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ applicant }) => {
      this.applicant = applicant;
    });
    console.error(this.applicant);
    /**
     * Finding dependencies for applicant
     */

    if (this.applicant?.id) {
      /**
       * NextOfKeen
       */
      this.applicantService.findNextOfKeen(this.applicant.id).subscribe(res => (this.nextOfKeen = res.body));
      /**
       * MarriageDetails
       */
      if (this.applicant.maritalStatus === MaritalStatus.MARRIED) {
        this.applicantService.findMarriageDetails(this.applicant.id).subscribe(res => (this.marriageDetails = res.body));
      }
      /**
       * Guardian
       */
      this.applicantService.findGuardian(this.applicant.id).subscribe(res => (this.guardian = res.body));
      /**
       * Declaration
       */
      this.applicantService.findDeclaration(this.applicant.id).subscribe(res => (this.declaration = res.body));
      /**
       * DemographicsDetails
       */
      this.applicantService.findDemographicDetails(this.applicant.id).subscribe(res => (this.demographicDetails = res.body));
    }
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }
  previousState(): void {
    window.history.back();
  }
}
