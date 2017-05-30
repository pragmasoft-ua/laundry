import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { WashPrice } from './wash-price.model';
import { WashPriceService } from './wash-price.service';

@Component({
    selector: 'jhi-wash-price-detail',
    templateUrl: './wash-price-detail.component.html'
})
export class WashPriceDetailComponent implements OnInit, OnDestroy {

    washPrice: WashPrice;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private washPriceService: WashPriceService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInWashPrices();
    }

    load(id) {
        this.washPriceService.find(id).subscribe((washPrice) => {
            this.washPrice = washPrice;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInWashPrices() {
        this.eventSubscriber = this.eventManager.subscribe(
            'washPriceListModification',
            (response) => this.load(this.washPrice.id)
        );
    }
}
