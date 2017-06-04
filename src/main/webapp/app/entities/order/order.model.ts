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

    calculateTotal() {
        const weight = this.weightKg || 0;
        const duration = this.durationHours || 0;
        const price = this.price ? this.price.priceKgHour : 0;
        this.total = weight * duration * price;
    }
}
