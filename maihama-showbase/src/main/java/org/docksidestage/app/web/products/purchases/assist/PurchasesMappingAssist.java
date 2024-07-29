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
package org.docksidestage.app.web.products.purchases.assist;

import java.util.List;
import java.util.stream.Collectors;

import org.docksidestage.app.web.products.purchases.PurchasesListResult;
import org.docksidestage.app.web.products.purchases.PurchasesListResult.PurchasesRowPart;
import org.docksidestage.app.web.products.purchases.PurchasesOneResult;
import org.docksidestage.dbflute.exentity.Purchase;

/**
 * @author jflute
 */
public class PurchasesMappingAssist {

    // ===================================================================================
    //                                                                         List Result
    //                                                                         ===========
    public PurchasesListResult mappingToListResult(List<Purchase> purchaseList) {
        List<PurchasesRowPart> rows = purchaseList.stream().map(product -> {
            return mappingToRowPart(product);
        }).collect(Collectors.toList());
        return new PurchasesListResult(rows);
    }

    private PurchasesRowPart mappingToRowPart(Purchase purchase) {
        PurchasesRowPart row = new PurchasesRowPart();
        row.purchaseId = purchase.getPurchaseId();
        purchase.getMember().alwaysPresent(member -> {
            row.memberName = member.getMemberName();
        });
        purchase.getProduct().alwaysPresent(product -> {
            row.productName = product.getProductName();
        });
        return row;
    }

    // ===================================================================================
    //                                                                          One Result
    //                                                                          ==========
    public PurchasesOneResult mappingToOneResult(Purchase purchase) {
        PurchasesOneResult result = new PurchasesOneResult();
        result.purchaseId = purchase.getPurchaseId();
        purchase.getMember().alwaysPresent(member -> {
            result.memberName = member.getMemberName();
        });
        purchase.getProduct().alwaysPresent(product -> {
            result.productName = product.getProductName();
        });
        return result;
    }
}
