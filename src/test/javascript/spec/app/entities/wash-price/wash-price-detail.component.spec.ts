import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { LaundryTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { WashPriceDetailComponent } from '../../../../../../main/webapp/app/entities/wash-price/wash-price-detail.component';
import { WashPriceService } from '../../../../../../main/webapp/app/entities/wash-price/wash-price.service';
import { WashPrice } from '../../../../../../main/webapp/app/entities/wash-price/wash-price.model';

describe('Component Tests', () => {

    describe('WashPrice Management Detail Component', () => {
        let comp: WashPriceDetailComponent;
        let fixture: ComponentFixture<WashPriceDetailComponent>;
        let service: WashPriceService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LaundryTestModule],
                declarations: [WashPriceDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    WashPriceService,
                    EventManager
                ]
            }).overrideComponent(WashPriceDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WashPriceDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WashPriceService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new WashPrice(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.washPrice).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
