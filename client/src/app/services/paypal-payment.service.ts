import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PaypalPaymentService {

  constructor(private http: HttpClient) { }

  /*makePayment(sum: string) {
    return this.http.post(this.url+'paypal/make/payment?sum='+sum, {})
      .map((response: Response) => response.json());
  }*/
}
