import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { WashMachine } from './wash-machine.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class WashMachineService {

    private resourceUrl = 'api/wash-machines';

    constructor(private http: Http) { }

    create(washMachine: WashMachine): Observable<WashMachine> {
        const copy = this.convert(washMachine);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(washMachine: WashMachine): Observable<WashMachine> {
        const copy = this.convert(washMachine);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<WashMachine> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(washMachine: WashMachine): WashMachine {
        const copy: WashMachine = Object.assign({}, washMachine);
        return copy;
    }
}
