import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LaundrySharedModule } from '../../shared';
import { LaundryAdminModule } from '../../admin/admin.module';
import {
    OrderService,
    OrderPopupService,
    OrderComponent,
    OrderDetailComponent,
    OrderDialogComponent,
    OrderPopupComponent,
    OrderDeletePopupComponent,
    OrderDeleteDialogComponent,
    orderRoute,
    orderPopupRoute,
} from './';

const ENTITY_STATES = [
    ...orderRoute,
    ...orderPopupRoute,
];

@NgModule({
    imports: [
        LaundrySharedModule,
        LaundryAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        OrderComponent,
        OrderDetailComponent,
        OrderDialogComponent,
        OrderDeleteDialogComponent,
        OrderPopupComponent,
        OrderDeletePopupComponent,
    ],
    entryComponents: [
        OrderComponent,
        OrderDialogComponent,
        OrderPopupComponent,
        OrderDeleteDialogComponent,
        OrderDeletePopupComponent,
    ],
    providers: [
        OrderService,
        OrderPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LaundryOrderModule {}
