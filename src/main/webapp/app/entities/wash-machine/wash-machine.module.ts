import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LaundrySharedModule } from '../../shared';
import {
    WashMachineService,
    WashMachinePopupService,
    WashMachineComponent,
    WashMachineDetailComponent,
    WashMachineDialogComponent,
    WashMachinePopupComponent,
    WashMachineDeletePopupComponent,
    WashMachineDeleteDialogComponent,
    washMachineRoute,
    washMachinePopupRoute,
} from './';

const ENTITY_STATES = [
    ...washMachineRoute,
    ...washMachinePopupRoute,
];

@NgModule({
    imports: [
        LaundrySharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        WashMachineComponent,
        WashMachineDetailComponent,
        WashMachineDialogComponent,
        WashMachineDeleteDialogComponent,
        WashMachinePopupComponent,
        WashMachineDeletePopupComponent,
    ],
    entryComponents: [
        WashMachineComponent,
        WashMachineDialogComponent,
        WashMachinePopupComponent,
        WashMachineDeleteDialogComponent,
        WashMachineDeletePopupComponent,
    ],
    providers: [
        WashMachineService,
        WashMachinePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LaundryWashMachineModule {}
