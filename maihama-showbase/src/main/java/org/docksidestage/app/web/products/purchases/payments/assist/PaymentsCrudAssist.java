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

import javax.annotation.Resource;

import org.docksidestage.app.web.products.purchases.payments.PaymentsSearchForm;
import org.docksidestage.dbflute.exbhv.PurchasePaymentBhv;
import org.docksidestage.dbflute.exentity.PurchasePayment;

/**
 * @author jflute
 */
public class PaymentsCrudAssist {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private PurchasePaymentBhv paymentBhv;

    // ===================================================================================
    //                                                                              Select
    //                                                                              ======
    public List<PurchasePayment> selectPaymentList(Integer productId, Long purchaseId, PaymentsSearchForm form) {
        return paymentBhv.selectList(cb -> {
            cb.setupSelect_Purchase();
            cb.query().queryPurchase().setProductId_Equal(productId);
            cb.query().setPurchaseId_Equal(purchaseId);
            cb.query().addOrderBy_PaymentDatetime_Desc();
        });
    }

    public PurchasePayment selectPaymentById(Integer productId, Long purchaseId, Long paymentId) {
        return paymentBhv.selectEntity(cb -> {
            cb.setupSelect_Purchase();
            cb.query().queryPurchase().setProductId_Equal(productId);
            cb.query().setPurchaseId_Equal(purchaseId);
            cb.query().setPurchasePaymentId_Equal(paymentId);
        }).get();
    }
}
