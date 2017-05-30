import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { WashPrice } from './wash-price.model';
import { WashPricePopupService } from './wash-price-popup.service';
import { WashPriceService } from './wash-price.service';

@Component({
    selector: 'jhi-wash-price-dialog',
    templateUrl: './wash-price-dialog.component.html'
})
export class WashPriceDialogComponent implements OnInit {

    washPrice: WashPrice;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private washPriceService: WashPriceService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.washPrice.id !== undefined) {
            this.subscribeToSaveResponse(
                this.washPriceService.update(this.washPrice));
        } else {
            this.subscribeToSaveResponse(
                this.washPriceService.create(this.washPrice));
        }
    }

    private subscribeToSaveResponse(result: Observable<WashPrice>) {
        result.subscribe((res: WashPrice) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: WashPrice) {
        this.eventManager.broadcast({ name: 'washPriceListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-wash-price-popup',
    template: ''
})
export class WashPricePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private washPricePopupService: WashPricePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.washPricePopupService
                    .open(WashPriceDialogComponent, params['id']);
            } else {
                this.modalRef = this.washPricePopupService
                    .open(WashPriceDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
