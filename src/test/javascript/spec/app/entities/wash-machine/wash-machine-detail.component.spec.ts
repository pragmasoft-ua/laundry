import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { LaundryTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { WashMachineDetailComponent } from '../../../../../../main/webapp/app/entities/wash-machine/wash-machine-detail.component';
import { WashMachineService } from '../../../../../../main/webapp/app/entities/wash-machine/wash-machine.service';
import { WashMachine } from '../../../../../../main/webapp/app/entities/wash-machine/wash-machine.model';

describe('Component Tests', () => {

    describe('WashMachine Management Detail Component', () => {
        let comp: WashMachineDetailComponent;
        let fixture: ComponentFixture<WashMachineDetailComponent>;
        let service: WashMachineService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [LaundryTestModule],
                declarations: [WashMachineDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    WashMachineService,
                    EventManager
                ]
            }).overrideTemplate(WashMachineDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WashMachineDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WashMachineService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new WashMachine(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.washMachine).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
