import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDeclaration, Declaration } from '../declaration.model';
import { DeclarationService } from '../service/declaration.service';

@Injectable({ providedIn: 'root' })
export class DeclarationRoutingResolveService implements Resolve<IDeclaration> {
  constructor(protected service: DeclarationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDeclaration> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((declaration: HttpResponse<Declaration>) => {
          if (declaration.body) {
            return of(declaration.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Declaration());
  }
}
