import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { OrderComponent } from './order.component';
import { OrderDetailComponent } from './order-detail.component';
import { OrderPopupComponent } from './order-dialog.component';
import { OrderDeletePopupComponent } from './order-delete-dialog.component';

import { Principal } from '../../shared';

export const orderRoute: Routes = [
    {
        path: 'order',
        component: OrderComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Orders'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'order/:id',
        component: OrderDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Orders'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const orderPopupRoute: Routes = [
    {
        path: 'order-new',
        component: OrderPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Orders'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'order/:id/edit',
        component: OrderPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Orders'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'order/:id/delete',
        component: OrderDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Orders'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
