import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDeclaration } from '../declaration.model';

@Component({
  selector: 'jhi-declaration-detail',
  templateUrl: './declaration-detail.component.html',
})
export class DeclarationDetailComponent implements OnInit {
  declaration: IDeclaration | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ declaration }) => {
      this.declaration = declaration;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
