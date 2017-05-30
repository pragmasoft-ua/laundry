import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { WashMachine } from './wash-machine.model';
import { WashMachinePopupService } from './wash-machine-popup.service';
import { WashMachineService } from './wash-machine.service';

@Component({
    selector: 'jhi-wash-machine-dialog',
    templateUrl: './wash-machine-dialog.component.html'
})
export class WashMachineDialogComponent implements OnInit {

    washMachine: WashMachine;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private washMachineService: WashMachineService,
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
        if (this.washMachine.id !== undefined) {
            this.subscribeToSaveResponse(
                this.washMachineService.update(this.washMachine));
        } else {
            this.subscribeToSaveResponse(
                this.washMachineService.create(this.washMachine));
        }
    }

    private subscribeToSaveResponse(result: Observable<WashMachine>) {
        result.subscribe((res: WashMachine) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: WashMachine) {
        this.eventManager.broadcast({ name: 'washMachineListModification', content: 'OK'});
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
    selector: 'jhi-wash-machine-popup',
    template: ''
})
export class WashMachinePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private washMachinePopupService: WashMachinePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.washMachinePopupService
                    .open(WashMachineDialogComponent, params['id']);
            } else {
                this.modalRef = this.washMachinePopupService
                    .open(WashMachineDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
