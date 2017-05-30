import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { WashPrice } from './wash-price.model';
import { WashPricePopupService } from './wash-price-popup.service';
import { WashPriceService } from './wash-price.service';

@Component({
    selector: 'jhi-wash-price-delete-dialog',
    templateUrl: './wash-price-delete-dialog.component.html'
})
export class WashPriceDeleteDialogComponent {

    washPrice: WashPrice;

    constructor(
        private washPriceService: WashPriceService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.washPriceService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'washPriceListModification',
                content: 'Deleted an washPrice'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-wash-price-delete-popup',
    template: ''
})
export class WashPriceDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private washPricePopupService: WashPricePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.washPricePopupService
                .open(WashPriceDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
