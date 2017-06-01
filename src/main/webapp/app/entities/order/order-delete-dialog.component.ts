import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { Order } from './order.model';
import { OrderPopupService } from './order-popup.service';
import { OrderService } from './order.service';

@Component({
    selector: 'jhi-order-delete-dialog',
    templateUrl: './order-delete-dialog.component.html'
})
export class OrderDeleteDialogComponent {

    order: Order;

    constructor(
        private orderService: OrderService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.orderService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'orderListModification',
                content: 'Deleted an order'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success(`A Order is deleted with identifier ${id}`, null, null);
    }
}

@Component({
    selector: 'jhi-order-delete-popup',
    template: ''
})
export class OrderDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private orderPopupService: OrderPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.orderPopupService
                .open(OrderDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
