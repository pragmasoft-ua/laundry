import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Order } from './order.model';
import { OrderPopupService } from './order-popup.service';
import { OrderService } from './order.service';
import { User, UserService } from '../../shared';
import { WashPrice, WashPriceService } from '../wash-price';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-order-dialog',
    templateUrl: './order-dialog.component.html'
})
export class OrderDialogComponent implements OnInit {

    order: Order;
    authorities: any[];
    isSaving: boolean;
    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private orderService: OrderService,
        private userService: UserService,
        private washPriceService: WashPriceService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.washPriceService.getCurrent()
            .subscribe((res: ResponseWrapper) => { this.order.price = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    calculateTotal() {
      this.order.calculateTotal();
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.order.id !== undefined) {
            this.subscribeToSaveResponse(
                this.orderService.update(this.order), false);
        } else {
            this.subscribeToSaveResponse(
                this.orderService.create(this.order), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Order>, isCreated: boolean) {
        result.subscribe((res: Order) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Order, isCreated: boolean) {
        this.alertService.success(
            isCreated ? `A new Order is created with identifier ${result.id}`
            : `A Order is updated with identifier ${result.id}`,
            null, null);

        this.eventManager.broadcast({ name: 'orderListModification', content: 'OK'});
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

    trackUserById(index: number, item: User) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-order-popup',
    template: ''
})
export class OrderPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private orderPopupService: OrderPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.orderPopupService
                    .open(OrderDialogComponent, params['id']);
            } else {
                this.modalRef = this.orderPopupService
                    .open(OrderDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
