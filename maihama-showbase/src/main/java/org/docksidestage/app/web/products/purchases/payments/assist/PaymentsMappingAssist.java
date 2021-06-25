/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.docksidestage.app.web.products.purchases.payments.assist;

import java.util.List;
import java.util.stream.Collectors;

import org.docksidestage.app.web.products.purchases.payments.PaymentsResult;
import org.docksidestage.app.web.products.purchases.payments.PaymentsRowResult;
import org.docksidestage.dbflute.exentity.PurchasePayment;

/**
 * @author jflute
 */
public class PaymentsMappingAssist {

    // ===================================================================================
    //                                                                         List Result
    //                                                                         ===========
    public List<PaymentsRowResult> mappingToListResult(List<PurchasePayment> paymentList) {
        return paymentList.stream().map(payment -> {
            return mappingToRowPart(payment);
        }).collect(Collectors.toList());
    }

    private PaymentsRowResult mappingToRowPart(PurchasePayment payment) {
        PaymentsRowResult row = new PaymentsRowResult();
        row.paymentId = payment.getPurchasePaymentId();
        row.productName = payment.getPurchase().get().getProduct().get().getProductName();
        return row;
    }

    // ===================================================================================
    //                                                                          One Result
    //                                                                          ==========
    public PaymentsResult mappingToOneResult(PurchasePayment payment) {
        PaymentsResult result = new PaymentsResult();
        result.paymentId = payment.getPurchasePaymentId();
        result.paymentDatetime = payment.getPaymentDatetime();
        return result;
    }
}
