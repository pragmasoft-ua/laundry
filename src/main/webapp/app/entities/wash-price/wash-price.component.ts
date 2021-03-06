import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService } from 'ng-jhipster';

import { WashPrice } from './wash-price.model';
import { WashPriceService } from './wash-price.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-wash-price',
    templateUrl: './wash-price.component.html'
})
export class WashPriceComponent implements OnInit, OnDestroy {
washPrices: WashPrice[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private washPriceService: WashPriceService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.washPriceService.query().subscribe(
            (res: ResponseWrapper) => {
                this.washPrices = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInWashPrices();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: WashPrice) {
        return item.id;
    }
    registerChangeInWashPrices() {
        this.eventSubscriber = this.eventManager.subscribe('washPriceListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
