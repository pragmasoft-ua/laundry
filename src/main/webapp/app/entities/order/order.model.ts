import { User } from '../../shared';
import { WashPrice } from '../wash-price';
import { WashMachine } from '../wash-machine';
export class Order {
    constructor(
        public id?: number,
        public startOn?: any,
        public durationHours?: number,
        public weightKg?: number,
        public total?: number,
        public customer?: User,
        public price?: WashPrice,
        public machine?: WashMachine,
    ) {
    }
}
