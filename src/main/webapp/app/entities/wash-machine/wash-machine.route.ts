import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { WashMachineComponent } from './wash-machine.component';
import { WashMachineDetailComponent } from './wash-machine-detail.component';
import { WashMachinePopupComponent } from './wash-machine-dialog.component';
import { WashMachineDeletePopupComponent } from './wash-machine-delete-dialog.component';

import { Principal } from '../../shared';

export const washMachineRoute: Routes = [
    {
        path: 'wash-machine',
        component: WashMachineComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WashMachines'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'wash-machine/:id',
        component: WashMachineDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WashMachines'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const washMachinePopupRoute: Routes = [
    {
        path: 'wash-machine-new',
        component: WashMachinePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WashMachines'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'wash-machine/:id/edit',
        component: WashMachinePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WashMachines'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'wash-machine/:id/delete',
        component: WashMachineDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WashMachines'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
