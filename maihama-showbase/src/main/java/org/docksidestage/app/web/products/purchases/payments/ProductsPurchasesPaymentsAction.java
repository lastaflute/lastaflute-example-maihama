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
package org.docksidestage.app.web.products.purchases.payments;

import java.util.List;

import org.docksidestage.app.web.base.ShowbaseBaseAction;
import org.docksidestage.app.web.products.purchases.payments.assist.PaymentsCrudAssist;
import org.docksidestage.app.web.products.purchases.payments.assist.PaymentsMappingAssist;
import org.docksidestage.dbflute.exentity.PurchasePayment;
import org.lastaflute.web.Execute;
import org.lastaflute.web.RestfulAction;
import org.lastaflute.web.login.AllowAnyoneAccess;
import org.lastaflute.web.response.JsonResponse;

import jakarta.annotation.Resource;

/**
 * @author jflute
 */
@AllowAnyoneAccess
@RestfulAction
public class ProductsPurchasesPaymentsAction extends ShowbaseBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private PaymentsCrudAssist paymentsCrudAssist;
    @Resource
    private PaymentsMappingAssist paymentsMappingAssist;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // /products/1/purchases/2/payments/
    //
    // http://localhost:8098/showbase/products/1/purchases/2/payments/?productName=S
    // _/_/_/_/_/_/_/_/_/_/
    @Execute
    public JsonResponse<List<PaymentsRowResult>> get$index(Integer productId, Long purchaseId, PaymentsSearchForm form) {
        validate(form, messages -> {});
        List<PurchasePayment> purchaseList = paymentsCrudAssist.selectPaymentList(productId, purchaseId, form);
        List<PaymentsRowResult> result = paymentsMappingAssist.mappingToListResult(purchaseList);
        return asJson(result); // example for wrapped list pattern
    }

    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // /products/1/purchases/2/payments/3/
    //
    // http://localhost:8098/showbase/products/1/purchases/16/payments/3/
    // _/_/_/_/_/_/_/_/_/_/
    @Execute
    public JsonResponse<PaymentsResult> get$index(Integer productId, Long purchaseId, Long paymentId) {
        PurchasePayment purchase = paymentsCrudAssist.selectPaymentById(productId, purchaseId, paymentId);
        PaymentsResult result = paymentsMappingAssist.mappingToOneResult(purchase);
        return asJson(result);
    }
}
