import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { LaundryWashMachineModule } from './wash-machine/wash-machine.module';
import { LaundryOrderModule } from './order/order.module';
import { LaundryWashPriceModule } from './wash-price/wash-price.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        LaundryWashMachineModule,
        LaundryOrderModule,
        LaundryWashPriceModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LaundryEntityModule {}
