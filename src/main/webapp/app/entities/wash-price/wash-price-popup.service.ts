import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { WashPrice } from './wash-price.model';
import { WashPriceService } from './wash-price.service';
@Injectable()
export class WashPricePopupService {
    private isOpen = false;
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private washPriceService: WashPriceService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.washPriceService.find(id).subscribe((washPrice) => {
                washPrice.efferctiveTo = this.datePipe
                    .transform(washPrice.efferctiveTo, 'yyyy-MM-ddThh:mm');
                this.washPriceModalRef(component, washPrice);
            });
        } else {
            return this.washPriceModalRef(component, new WashPrice());
        }
    }

    washPriceModalRef(component: Component, washPrice: WashPrice): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.washPrice = washPrice;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
