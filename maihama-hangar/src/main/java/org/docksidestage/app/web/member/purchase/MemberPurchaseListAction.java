/*
 * Copyright 2014-2015 the original author or authors.
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

import javax.annotation.Resource;

import org.dbflute.cbean.result.PagingResultBean;
import org.dbflute.exception.EntityAlreadyDeletedException;
import org.dbflute.exception.EntityDuplicatedException;
import org.dbflute.optional.OptionalEntity;
import org.dbflute.optional.OptionalThing;
import org.docksidestage.app.web.base.HangarBaseAction;
import org.docksidestage.app.web.base.paging.SearchPagingBean;
import org.docksidestage.dbflute.cbean.PurchaseCB;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exbhv.PurchaseBhv;
import org.docksidestage.dbflute.exentity.Member;
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
    // -----------------------------------------------------
    //                                          DI Component
    //                                          ------------
    @Resource
    private MemberBhv memberBhv;

    @Resource
    private PurchaseBhv purchaseBhv;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<SearchPagingBean<MemberPurchaseSearchRowBean>> index(Integer memberId, OptionalThing<Integer> pageNumber,
            MemberPurchaseListBody body) {
        Integer pageNumberValue = pageNumber.orElse(1);
        PagingResultBean<Purchase> page = selectPurchasePage(memberId, pageNumberValue);

        SearchPagingBean<MemberPurchaseSearchRowBean> bean = new SearchPagingBean<>(page);
        bean.items = page.mappingList(purchase -> {
            return mappingToBean(purchase);
        });

        return asJson(bean);
    }

    @Execute
    public JsonResponse<Void> delete(MemberPurchaseListBody form) {
        validate(form, messages -> {});

        try {
            Purchase purchase = new Purchase();
            purchase.setPurchaseId(form.purchaseId);
            purchaseBhv.deleteNonstrict(purchase); // no optimistic lock
        } catch (EntityAlreadyDeletedException e) {
            // already deleted
        } catch (EntityDuplicatedException e) {
            // entity duplicated
        }

        return JsonResponse.asEmptyBody();
    }

    // ===================================================================================
    //                                                                              Select
    //                                                                              ======
    protected OptionalEntity<Member> selectMember(Integer memberId) {
        return memberBhv.selectEntity(cb -> {
            cb.query().setMemberId_Equal(memberId);
        });
    }

    protected PagingResultBean<Purchase> selectPurchasePage(Integer memberId, Integer pageNumber) {
        return purchaseBhv.selectPage(cb -> {
            setupMemberPurchaseCB(memberId, cb);
            cb.paging(getPagingPageSize(), pageNumber);
        });
    }

    protected Integer selectCount(Integer memberId, Integer pageNumber) {
        return purchaseBhv.selectCount(cb -> {
            setupMemberPurchaseCB(memberId, cb);
        });
    }

    private void setupMemberPurchaseCB(Integer memberId, PurchaseCB cb) {
        cb.setupSelect_Product();
        cb.query().setMemberId_Equal(memberId);
        cb.query().addOrderBy_PurchaseDatetime_Desc();
    }

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    protected MemberPurchaseSearchRowBean mappingToBean(Purchase purchase) {
        MemberPurchaseSearchRowBean bean = new MemberPurchaseSearchRowBean();
        bean.purchaseId = purchase.getPurchaseId();
        bean.purchaseDatetime = purchase.getPurchaseDatetime();
        bean.productName = purchase.getProduct().get().getProductName();
        bean.purchasePrice = purchase.getPurchasePrice();
        bean.purchaseCount = purchase.getPurchaseCount();
        bean.paymentComplete = purchase.isPaymentCompleteFlgTrue();
        return bean;
    }
}
