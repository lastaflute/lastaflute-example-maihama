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
package org.docksidestage.app.web.products.purchases;

import java.util.List;

import javax.annotation.Resource;

import org.docksidestage.app.web.base.ShowbaseBaseAction;
import org.docksidestage.app.web.products.purchases.assist.PurchasesCrudAssist;
import org.docksidestage.app.web.products.purchases.assist.PurchasesMappingAssist;
import org.docksidestage.dbflute.exentity.Purchase;
import org.lastaflute.web.Execute;
import org.lastaflute.web.RestfulAction;
import org.lastaflute.web.login.AllowAnyoneAccess;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author jflute
 */
@AllowAnyoneAccess
@RestfulAction
public class ProductsPurchasesAction extends ShowbaseBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private PurchasesCrudAssist purchasesCrudAssist;
    @Resource
    private PurchasesMappingAssist purchasesMappingAssist;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // /products/1/purchases/
    //
    // *it needs "list" in method, which is resolved by ActionAdjustmentProvider
    // http://localhost:8151/fortress/products/1/purchases/?memberName=S
    // _/_/_/_/_/_/_/_/_/_/
    @Execute
    public JsonResponse<PurchasesListResult> get$index(Integer productId, PurchasesListForm form) {
        validateApi(form, messages -> {});
        List<Purchase> purchaseList = selectPurchaseList(productId, form);
        PurchasesListResult result = mappingToListResult(purchaseList);
        return asJson(result);
    }

    @Execute
    public JsonResponse<PurchasesListResult> get$sea(Integer productId, PurchasesListForm form) {
        validateApi(form, messages -> {});
        List<Purchase> purchaseList = selectPurchaseList(productId, form);
        PurchasesListResult result = mappingToListResult(purchaseList);
        return asJson(result);
    }

    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // /products/1/purchases/2/
    //
    // http://localhost:8151/fortress/products/1/purchases/16/
    // _/_/_/_/_/_/_/_/_/_/
    @Execute
    public JsonResponse<PurchasesOneResult> get$index(Integer productId, Long purchaseId) {
        Purchase purchase = selectPurchaseById(productId, purchaseId);
        PurchasesOneResult result = mappingToOneResult(purchase);
        return asJson(result);
    }

    // ===================================================================================
    //                                                                              Select
    //                                                                              ======
    private List<Purchase> selectPurchaseList(Integer productId, PurchasesListForm form) {
        return purchasesCrudAssist.selectPurchaseList(productId, form);
    }

    private Purchase selectPurchaseById(Integer productId, Long purchaseId) {
        return purchasesCrudAssist.selectPurchaseById(productId, purchaseId);
    }

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    private PurchasesListResult mappingToListResult(List<Purchase> purchaseList) {
        return purchasesMappingAssist.mappingToListResult(purchaseList);
    }

    private PurchasesOneResult mappingToOneResult(Purchase purchase) {
        return purchasesMappingAssist.mappingToOneResult(purchase);
    }
}
