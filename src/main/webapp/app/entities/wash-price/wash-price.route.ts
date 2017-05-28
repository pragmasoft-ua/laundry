import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { WashPriceComponent } from './wash-price.component';
import { WashPriceDetailComponent } from './wash-price-detail.component';
import { WashPricePopupComponent } from './wash-price-dialog.component';
import { WashPriceDeletePopupComponent } from './wash-price-delete-dialog.component';

import { Principal } from '../../shared';

export const washPriceRoute: Routes = [
    {
        path: 'wash-price',
        component: WashPriceComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'laundryApp.washPrice.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'wash-price/:id',
        component: WashPriceDetailComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'laundryApp.washPrice.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const washPricePopupRoute: Routes = [
    {
        path: 'wash-price-new',
        component: WashPricePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'laundryApp.washPrice.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'wash-price/:id/edit',
        component: WashPricePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'laundryApp.washPrice.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'wash-price/:id/delete',
        component: WashPriceDeletePopupComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'laundryApp.washPrice.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
