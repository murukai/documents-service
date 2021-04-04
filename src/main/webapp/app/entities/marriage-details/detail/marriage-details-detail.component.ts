import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IApplicant } from 'app/entities/applicant/applicant.model';
import { ApplicantService } from 'app/entities/applicant/service/applicant.service';

import { IMarriageDetails } from '../marriage-details.model';

@Component({
  selector: 'jhi-marriage-details-detail',
  templateUrl: './marriage-details-detail.component.html',
})
export class MarriageDetailsDetailComponent implements OnInit {
  marriageDetails: IMarriageDetails | null = null;
  applicant: IApplicant | null = null;

  constructor(protected activatedRoute: ActivatedRoute, protected applicantService: ApplicantService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ marriageDetails }) => {
      this.marriageDetails = marriageDetails;
      if (marriageDetails.applicant.id) {
        this.applicantService.find(marriageDetails.applicant.id).subscribe(res => (this.applicant = res.body));
      }
    });
  }

  previousState(): void {
    window.history.back();
  }
}
