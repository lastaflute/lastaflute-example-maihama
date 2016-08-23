/*
 * Copyright 2015-2016 the original author or authors.
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
package org.docksidestage.app.web.member.purchase;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.cbean.result.PagingResultBean;
import org.dbflute.optional.OptionalThing;
import org.docksidestage.app.web.base.HangarBaseAction;
import org.docksidestage.app.web.base.paging.PagingAssist;
import org.docksidestage.app.web.base.paging.SearchPagingResult;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exbhv.PurchaseBhv;
import org.docksidestage.dbflute.exentity.Purchase;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author jflute
 * @author iwamatsu0430
 */
public class MemberPurchaseListAction extends HangarBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private MemberBhv memberBhv;
    @Resource
    private PurchaseBhv purchaseBhv;
    @Resource
    private PagingAssist pagingAssist;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<SearchPagingResult<MemberPurchaseSearchRowResult>> index(Integer memberId, OptionalThing<Integer> pageNumber,
            MemberPurchaseListBody body) {
        PagingResultBean<Purchase> page = selectPurchasePage(memberId, pageNumber.orElse(1));
        List<MemberPurchaseSearchRowResult> rows = page.stream().map(purchase -> {
            return convertToRowResult(purchase);
        }).collect(Collectors.toList());
        SearchPagingResult<MemberPurchaseSearchRowResult> result = pagingAssist.createPagingResult(page, rows);
        return asJson(result);
    }

    @Execute
    public JsonResponse<Void> delete(MemberPurchaseListBody body) {
        validate(body, messages -> {});
        Purchase purchase = new Purchase();
        purchase.setPurchaseId(body.purchaseId);
        purchaseBhv.deleteNonstrict(purchase);
        return JsonResponse.asEmptyBody();
    }

    // ===================================================================================
    //                                                                              Select
    //                                                                              ======
    protected PagingResultBean<Purchase> selectPurchasePage(Integer memberId, Integer pageNumber) {
        return purchaseBhv.selectPage(cb -> {
            cb.setupSelect_Product();
            cb.query().setMemberId_Equal(memberId);
            cb.query().addOrderBy_PurchaseDatetime_Desc();
            cb.paging(4, pageNumber);
        });
    }

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    private MemberPurchaseSearchRowResult convertToRowResult(Purchase purchase) {
        MemberPurchaseSearchRowResult result = new MemberPurchaseSearchRowResult();
        result.purchaseId = purchase.getPurchaseId();
        result.purchaseDatetime = purchase.getPurchaseDatetime();
        result.productName = purchase.getProduct().get().getProductName();
        result.purchasePrice = purchase.getPurchasePrice();
        result.purchaseCount = purchase.getPurchaseCount();
        result.paymentComplete = purchase.isPaymentCompleteFlgTrue();
        return result;
    }
}
