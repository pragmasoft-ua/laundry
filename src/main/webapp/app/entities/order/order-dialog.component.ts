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
import { WashMachine, WashMachineService } from '../wash-machine';
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

    washprices: WashPrice[];

    washmachines: WashMachine[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private orderService: OrderService,
        private userService: UserService,
        private washPriceService: WashPriceService,
        private washMachineService: WashMachineService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.washPriceService.query()
            .subscribe((res: ResponseWrapper) => { this.washprices = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.washMachineService.query()
            .subscribe((res: ResponseWrapper) => { this.washmachines = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.order.id !== undefined) {
            this.subscribeToSaveResponse(
                this.orderService.update(this.order));
        } else {
            this.subscribeToSaveResponse(
                this.orderService.create(this.order));
        }
    }

    private subscribeToSaveResponse(result: Observable<Order>) {
        result.subscribe((res: Order) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Order) {
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

    trackWashPriceById(index: number, item: WashPrice) {
        return item.id;
    }

    trackWashMachineById(index: number, item: WashMachine) {
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
