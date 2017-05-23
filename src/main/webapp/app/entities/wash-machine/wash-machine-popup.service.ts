import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { WashMachine } from './wash-machine.model';
import { WashMachineService } from './wash-machine.service';
@Injectable()
export class WashMachinePopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private washMachineService: WashMachineService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.washMachineService.find(id).subscribe((washMachine) => {
                this.washMachineModalRef(component, washMachine);
            });
        } else {
            return this.washMachineModalRef(component, new WashMachine());
        }
    }

    washMachineModalRef(component: Component, washMachine: WashMachine): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.washMachine = washMachine;
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
