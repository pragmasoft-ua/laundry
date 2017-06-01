import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, AlertService } from 'ng-jhipster';

import { WashMachine } from './wash-machine.model';
import { WashMachineService } from './wash-machine.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-wash-machine',
    templateUrl: './wash-machine.component.html'
})
export class WashMachineComponent implements OnInit, OnDestroy {
washMachines: WashMachine[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private washMachineService: WashMachineService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.washMachineService.query().subscribe(
            (res: ResponseWrapper) => {
                this.washMachines = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInWashMachines();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: WashMachine) {
        return item.id;
    }
    registerChangeInWashMachines() {
        this.eventSubscriber = this.eventManager.subscribe('washMachineListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
