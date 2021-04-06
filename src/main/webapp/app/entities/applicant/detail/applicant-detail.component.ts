import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IAppointmentSlot } from 'app/entities/appointment-slot/appointment-slot.model';
import { AppointmentSlotService } from 'app/entities/appointment-slot/service/appointment-slot.service';
import { IDeclaration } from 'app/entities/declaration/declaration.model';
import { DeclarationService } from 'app/entities/declaration/service/declaration.service';
import { IDemographicDetails } from 'app/entities/demographic-details/demographic-details.model';
import { DemographicDetailsService } from 'app/entities/demographic-details/service/demographic-details.service';
import { IGuardian } from 'app/entities/guardian/guardian.model';
import { GuardianService } from 'app/entities/guardian/service/guardian.service';
import { IMarriageDetails } from 'app/entities/marriage-details/marriage-details.model';
import { MarriageDetailsService } from 'app/entities/marriage-details/service/marriage-details.service';
import { INextOfKeen } from 'app/entities/next-of-keen/next-of-keen.model';
import { NextOfKeenService } from 'app/entities/next-of-keen/service/next-of-keen.service';

import { IApplicant } from '../applicant.model';

@Component({
  selector: 'jhi-applicant-detail',
  templateUrl: './applicant-detail.component.html',
  styleUrls: ['./applicant-detail.component.css'],
})
export class ApplicantDetailComponent implements OnInit {
  applicant: IApplicant | null = null;
  democraphicDetails: IDemographicDetails | null = null;
  declaration: IDeclaration | null = null;
  marriageDetails: IMarriageDetails | null = null;
  nextOfKeen: INextOfKeen | null = null;
  appointmentSlot: IAppointmentSlot | null = null;
  guardian: IGuardian | null = null;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected declarationService: DeclarationService,
    protected guardianService: GuardianService,
    protected democraphicDetailsService: DemographicDetailsService,
    protected marriageDetailsService: MarriageDetailsService,
    protected nextOfKeenService: NextOfKeenService,
    protected appointmentSlotService: AppointmentSlotService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ applicant }) => {
      this.applicant = applicant;
      if (applicant.democraphicDetails) {
        this.democraphicDetailsService
          .find(applicant.democraphicDetails.id)
          .subscribe((res: HttpResponse<IDemographicDetails>) => (this.democraphicDetails = res.body));
      }
      console.error('Found Demographic details id is ', applicant.democraphicDetails.id);
      const declarationId = applicant.declaration.id;
      console.error('The Applicant', applicant);
      console.error('Declaration id is : {}', declarationId);
      if (applicant.declaration) {
        this.declarationService
          .find(applicant.declaration.id)
          .subscribe((res: HttpResponse<IDeclaration>) => (this.declaration = res.body));
      }
      const guardianId = applicant.guardian.id;
      console.error('Guardian Id is ', guardianId);
      if (applicant.guardian) {
        this.guardianService.find(applicant.guardian.id).subscribe((res: HttpResponse<IGuardian>) => (this.guardian = res.body));
      }

      console.error('MarriageDetails is ', applicant.marriageDetails);
      if (applicant.marriageDetails) {
        this.marriageDetailsService
          .find(applicant.marriageDetails.id)
          .subscribe((res: HttpResponse<IMarriageDetails>) => (this.marriageDetails = res.body));
      }
      console.error('Next of Keen is ', applicant.nextOfKeen);
      if (applicant.nextOfKeen) {
        this.nextOfKeenService.find(applicant.nextOfKeen.id).subscribe((res: HttpResponse<INextOfKeen>) => (this.nextOfKeen = res.body));
      }

      const appointmentSlotId = applicant.appointmentSlot.id;
      console.error('Appointment Slot id is ', appointmentSlotId);
      if (applicant.appointmentSlot.id) {
        this.appointmentSlotService
          .find(applicant.appointmentSlot.id)
          .subscribe((res: HttpResponse<IAppointmentSlot>) => (this.appointmentSlot = res.body));
      }
      console.error('Appointment slot is ', this.appointmentSlot);
    });
  }

  previousState(): void {
    window.history.back();
  }
}
