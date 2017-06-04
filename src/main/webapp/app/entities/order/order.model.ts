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
        const oldTotal = this.total;
        try {this.total = this.weightKg * this.durationHours * this.price.priceKgHour; } catch (error) { this.total = oldTotal; }
    }
}
