import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { WashMachine } from './wash-machine.model';
import { WashMachineService } from './wash-machine.service';

@Component({
    selector: 'jhi-wash-machine-detail',
    templateUrl: './wash-machine-detail.component.html'
})
export class WashMachineDetailComponent implements OnInit, OnDestroy {

    washMachine: WashMachine;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private washMachineService: WashMachineService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInWashMachines();
    }

    load(id) {
        this.washMachineService.find(id).subscribe((washMachine) => {
            this.washMachine = washMachine;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInWashMachines() {
        this.eventSubscriber = this.eventManager.subscribe(
            'washMachineListModification',
            (response) => this.load(this.washMachine.id)
        );
    }
}
