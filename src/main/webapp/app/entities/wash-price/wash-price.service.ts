import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { DateUtils } from 'ng-jhipster';

import { WashPrice } from './wash-price.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class WashPriceService {

    private resourceUrl = 'api/wash-prices';
    private currentPriceUrl = 'api/current-price';

    constructor(private http: Http, private dateUtils: DateUtils) { }

    create(washPrice: WashPrice): Observable<WashPrice> {
        const copy = this.convert(washPrice);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(washPrice: WashPrice): Observable<WashPrice> {
        const copy = this.convert(washPrice);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<WashPrice> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    getCurrent(): Observable<ResponseWrapper> {
        return this.http.get(this.currentPriceUrl)
        .map((res: Response) => {
            const jsonResponse = res.json();
            if (jsonResponse.efferctiveTo) { this.convertItemFromServer(jsonResponse); }
            return new ResponseWrapper(res.headers, jsonResponse, res.status);
        });
    }

    setCurrent(price: number): Observable<Response> {
        return this.http.put(this.currentPriceUrl, price);
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
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.efferctiveTo = this.dateUtils
            .convertDateTimeFromServer(entity.efferctiveTo);
    }

    private convert(washPrice: WashPrice): WashPrice {
        const copy: WashPrice = Object.assign({}, washPrice);

        copy.efferctiveTo = this.dateUtils.toDate(washPrice.efferctiveTo);
        return copy;
    }
}
