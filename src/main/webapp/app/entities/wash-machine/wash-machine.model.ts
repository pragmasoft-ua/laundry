export class WashMachine {
    constructor(
        public id?: number,
        public capacity?: number,
        public available?: boolean,
    ) {
        this.available = false;
    }
}
