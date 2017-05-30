import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LaundrySharedModule } from '../../shared';
import {
    WashPriceService,
    WashPricePopupService,
    WashPriceComponent,
    WashPriceDetailComponent,
    WashPriceDialogComponent,
    WashPricePopupComponent,
    WashPriceDeletePopupComponent,
    WashPriceDeleteDialogComponent,
    washPriceRoute,
    washPricePopupRoute,
} from './';

const ENTITY_STATES = [
    ...washPriceRoute,
    ...washPricePopupRoute,
];

@NgModule({
    imports: [
        LaundrySharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        WashPriceComponent,
        WashPriceDetailComponent,
        WashPriceDialogComponent,
        WashPriceDeleteDialogComponent,
        WashPricePopupComponent,
        WashPriceDeletePopupComponent,
    ],
    entryComponents: [
        WashPriceComponent,
        WashPriceDialogComponent,
        WashPricePopupComponent,
        WashPriceDeleteDialogComponent,
        WashPriceDeletePopupComponent,
    ],
    providers: [
        WashPriceService,
        WashPricePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LaundryWashPriceModule {}
