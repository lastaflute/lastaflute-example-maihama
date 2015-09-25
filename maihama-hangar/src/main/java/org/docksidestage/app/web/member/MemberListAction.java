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
package org.docksidestage.app.web.member;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.dbflute.cbean.result.ListResultBean;
import org.dbflute.cbean.result.PagingResultBean;
import org.dbflute.optional.OptionalThing;
import org.docksidestage.app.web.base.HangarBaseAction;
import org.docksidestage.app.web.base.paging.SearchPagingBean;
import org.docksidestage.dbflute.allcommon.CDef;
import org.docksidestage.dbflute.cbean.MemberCB;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exbhv.MemberStatusBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.docksidestage.dbflute.exentity.MemberStatus;
import org.lastaflute.web.Execute;
import org.lastaflute.web.callback.ActionRuntime;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author jflute
 * @author iwamatsu0430
 */
public class MemberListAction extends HangarBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // -----------------------------------------------------
    //                                          DI Component
    //                                          ------------
    @Resource
    protected MemberBhv memberBhv;

    @Resource
    protected MemberStatusBhv memberStatusBhv;

    // -----------------------------------------------------
    //                                          Display Data
    //                                          ------------
    public Map<String, String> memberStatusMap;

    // ===================================================================================
    //                                                                               Hook
    //                                                                              ======
    @Override
    protected void setupHtmlData(ActionRuntime runtime) {
        super.setupHtmlData(runtime);
        prepareListBox();
    }

    protected void prepareListBox() {
        ListResultBean<MemberStatus> statusList = memberStatusBhv.selectList(cb -> {
            cb.query().addOrderBy_DisplayOrder_Asc();
        });
        Map<String, String> statusMap = new LinkedHashMap<String, String>();
        statusMap.put("", "select");
        statusList.forEach(status -> statusMap.put(status.getMemberStatusCode(), status.getMemberStatusName()));
        memberStatusMap = statusMap;
    }

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<SearchPagingBean<MemberSearchRowBean>> index(OptionalThing<Integer> pageNumber, MemberSearchBody body) {
        validate(body, messages -> {});

        Integer pageNumberValue = pageNumber.orElse(1);
        PagingResultBean<Member> page = selectMemberPage(pageNumberValue, body);

        SearchPagingBean<MemberSearchRowBean> bean = new SearchPagingBean<>(page);
        bean.items = page.mappingList(product -> {
            return mappingToBean(product);
        });

        return asJson(bean);
    }

    // ===================================================================================
    //                                                                              Select
    //                                                                              ======
    protected PagingResultBean<Member> selectMemberPage(int pageNumber, MemberSearchBody body) {
        return memberBhv.selectPage(cb -> {
            setupMemberCB(body, cb);

            cb.query().addOrderBy_UpdateDatetime_Desc();
            cb.query().addOrderBy_MemberId_Asc();

            int pageSize = getPagingPageSize();
            cb.paging(pageSize, pageNumber);
        });
    }

    private void setupMemberCB(MemberSearchBody form, MemberCB cb) {
        cb.ignoreNullOrEmptyQuery();
        cb.setupSelect_MemberStatus();
        cb.specify().derivedPurchase().count(purchaseCB -> {
            purchaseCB.specify().columnPurchaseId();
        } , Member.ALIAS_purchaseCount);

        cb.query().setMemberName_LikeSearch(form.memberName, op -> op.likeContain());
        final String purchaseProductName = form.purchaseProductName;
        final boolean unpaid = form.unpaid;
        if ((purchaseProductName != null && purchaseProductName.trim().length() > 0) || unpaid) {
            cb.query().existsPurchase(purchaseCB -> {
                purchaseCB.query().queryProduct().setProductName_LikeSearch(purchaseProductName, op -> op.likeContain());
                if (unpaid) {
                    purchaseCB.query().setPaymentCompleteFlg_Equal_False();
                }
            });
        }
        cb.query().setMemberStatusCode_Equal_AsMemberStatus(CDef.MemberStatus.codeOf(form.memberStatus));
        LocalDateTime formalizedDateFrom = toDateTime(form.formalizedDateFrom).orElse(null);
        LocalDateTime formalizedDateTo = toDateTime(form.formalizedDateTo).orElse(null);
        cb.query().setFormalizedDatetime_FromTo(formalizedDateFrom, formalizedDateTo, op -> op.compareAsDate());
    }

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    private MemberSearchRowBean mappingToBean(Member member) {
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
