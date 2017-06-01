import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { WashMachine } from './wash-machine.model';
import { WashMachinePopupService } from './wash-machine-popup.service';
import { WashMachineService } from './wash-machine.service';

@Component({
    selector: 'jhi-wash-machine-delete-dialog',
    templateUrl: './wash-machine-delete-dialog.component.html'
})
export class WashMachineDeleteDialogComponent {

    washMachine: WashMachine;

    constructor(
        private washMachineService: WashMachineService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.washMachineService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'washMachineListModification',
                content: 'Deleted an washMachine'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success(`A Wash Machine is deleted with identifier ${id}`, null, null);
    }
}

@Component({
    selector: 'jhi-wash-machine-delete-popup',
    template: ''
})
export class WashMachineDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private washMachinePopupService: WashMachinePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.washMachinePopupService
                .open(WashMachineDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
