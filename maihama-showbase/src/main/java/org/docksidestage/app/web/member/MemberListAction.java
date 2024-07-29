/*
 * Copyright 2015-2022 the original author or authors.
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

import org.dbflute.cbean.result.PagingResultBean;
import org.dbflute.optional.OptionalThing;
import org.docksidestage.app.web.base.ShowbaseBaseAction;
import org.docksidestage.app.web.base.paging.PagingAssist;
import org.docksidestage.app.web.base.paging.SearchPagingResult;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.lastaflute.core.util.LaStringUtil;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

import jakarta.annotation.Resource;

/**
 * @author jflute
 * @author iwamatsu0430
 * @author black-trooper
 */
public class MemberListAction extends ShowbaseBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private MemberBhv memberBhv;
    @Resource
    private PagingAssist pagingAssist;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<SearchPagingResult<MemberSearchRowResult>> index(OptionalThing<Integer> pageNumber, MemberSearchBody body) {
        validate(body, messages -> {});
        PagingResultBean<Member> page = selectMemberPage(pageNumber.orElse(1), body);
        SearchPagingResult<MemberSearchRowResult> result = mappingToResult(page);
        return asJson(result);
    }

    // ===================================================================================
    //                                                                              Select
    //                                                                              ======
    private PagingResultBean<Member> selectMemberPage(int pageNumber, MemberSearchBody body) {
        verifyOrIllegalTransition("The pageNumber should be positive number: " + pageNumber, pageNumber > 0);
        return memberBhv.selectPage(cb -> {
            cb.setupSelect_MemberStatus();
            cb.specify().derivedPurchase().count(purchaseCB -> {
                purchaseCB.specify().columnPurchaseId();
            }, Member.ALIAS_purchaseCount);
            if (LaStringUtil.isNotEmpty(body.memberName)) {
                cb.query().setMemberName_LikeSearch(body.memberName, op -> op.likeContain());
            }
            if (LaStringUtil.isNotEmpty(body.purchaseProductName) || body.unpaid) {
                cb.query().existsPurchase(purchaseCB -> {
                    if (LaStringUtil.isNotEmpty(body.purchaseProductName)) {
                        purchaseCB.query().queryProduct().setProductName_LikeSearch(body.purchaseProductName, op -> op.likeContain());
                    }
                    if (body.unpaid) {
                        purchaseCB.query().setPaymentCompleteFlg_Equal_False();
                    }
                });
            }
            if (body.memberStatus != null) {
                cb.query().setMemberStatusCode_Equal_AsMemberStatus(body.memberStatus);
            }
            if (body.formalizedFrom != null || body.formalizedTo != null) {
                LocalDateTime fromTime = body.formalizedFrom != null ? body.formalizedFrom.atStartOfDay() : null;
                LocalDateTime toTime = body.formalizedTo != null ? body.formalizedTo.atStartOfDay() : null;
                cb.query().setFormalizedDatetime_FromTo(fromTime, toTime, op -> op.compareAsDate().allowOneSide());
            }
            cb.query().addOrderBy_UpdateDatetime_Desc();
            cb.query().addOrderBy_MemberId_Asc();
            cb.paging(4, pageNumber);
        });
    }

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    private SearchPagingResult<MemberSearchRowResult> mappingToResult(PagingResultBean<Member> page) {
        return pagingAssist.createPagingResult(page, page.mappingList(product -> {
            return convertToRowResult(product);
        }));
    }

    private MemberSearchRowResult convertToRowResult(Member member) {
        MemberSearchRowResult result = new MemberSearchRowResult();
        result.memberId = member.getMemberId();
        result.memberName = member.getMemberName();
        member.getMemberStatus().alwaysPresent(status -> {
            result.memberStatusName = status.getMemberStatusName();
        });
        LocalDateTime formalizedDatetime = member.getFormalizedDatetime();
        if (formalizedDatetime != null) {
            result.formalizedDate = formalizedDatetime.toLocalDate();
        }
        result.updateDatetime = member.getUpdateDatetime();
        result.withdrawalMember = member.isMemberStatusCodeWithdrawal();
        result.purchaseCount = member.getPurchaseCount();
        return result;
    }
}
