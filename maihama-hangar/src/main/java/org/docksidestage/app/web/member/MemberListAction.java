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
package org.docksidestage.app.web.member;

import java.time.LocalDateTime;

import javax.annotation.Resource;

import org.dbflute.cbean.result.PagingResultBean;
import org.dbflute.optional.OptionalThing;
import org.docksidestage.app.web.base.HangarBaseAction;
import org.docksidestage.app.web.base.paging.SearchPagingBean;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author jflute
 * @author iwamatsu0430
 */
public class MemberListAction extends HangarBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private MemberBhv memberBhv;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<SearchPagingBean<MemberSearchRowBean>> index(OptionalThing<Integer> pageNumber, MemberSearchBody body) {
        validate(body, messages -> {});
        PagingResultBean<Member> page = selectMemberPage(pageNumber.orElse(1), body);
        SearchPagingBean<MemberSearchRowBean> bean = mappingToBean(page);
        return asJson(bean);
    }

    // ===================================================================================
    //                                                                              Select
    //                                                                              ======
    private PagingResultBean<Member> selectMemberPage(int pageNumber, MemberSearchBody body) {
        return memberBhv.selectPage(cb -> {
            cb.ignoreNullOrEmptyQuery();
            cb.setupSelect_MemberStatus();
            cb.specify().derivedPurchase().count(purchaseCB -> {
                purchaseCB.specify().columnPurchaseId();
            } , Member.ALIAS_purchaseCount);

            cb.query().setMemberName_LikeSearch(body.memberName, op -> op.likeContain());
            String purchaseProductName = body.purchaseProductName;
            boolean unpaid = body.unpaid;
            if ((purchaseProductName != null && purchaseProductName.trim().length() > 0) || unpaid) {
                cb.query().existsPurchase(purchaseCB -> {
                    purchaseCB.query().queryProduct().setProductName_LikeSearch(purchaseProductName, op -> op.likeContain());
                    if (unpaid) {
                        purchaseCB.query().setPaymentCompleteFlg_Equal_False();
                    }
                });
            }
            cb.query().setMemberStatusCode_Equal_AsMemberStatus(body.memberStatus);
            LocalDateTime formalizedDateFrom = toDateTime(body.formalizedDateFrom).orElse(null);
            LocalDateTime formalizedDateTo = toDateTime(body.formalizedDateTo).orElse(null);
            cb.query().setFormalizedDatetime_FromTo(formalizedDateFrom, formalizedDateTo, op -> op.compareAsDate());

            cb.query().addOrderBy_UpdateDatetime_Desc();
            cb.query().addOrderBy_MemberId_Asc();

            cb.paging(getPagingPageSize(), pageNumber);
        });
    }

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    private SearchPagingBean<MemberSearchRowBean> mappingToBean(PagingResultBean<Member> page) {
        return createPagingBean(page, page.mappingList(product -> {
            return convertToRowBean(product);
        }));
    }

    private MemberSearchRowBean convertToRowBean(Member member) {
        MemberSearchRowBean bean = new MemberSearchRowBean();
        bean.memberId = member.getMemberId();
        bean.memberName = member.getMemberName();
        member.getMemberStatus().alwaysPresent(status -> {
            bean.memberStatusName = status.getMemberStatusName();
        });
        bean.formalizedDate = toStringDate(member.getFormalizedDatetime()).orElse(null);
        bean.updateDatetime = toStringDate(member.getUpdateDatetime()).get();
        bean.withdrawalMember = member.isMemberStatusCodeWithdrawal();
        bean.purchaseCount = member.getPurchaseCount();
        return bean;
    }
}
