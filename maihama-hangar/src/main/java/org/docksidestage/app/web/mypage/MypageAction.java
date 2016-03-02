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
package org.docksidestage.app.web.mypage;

import javax.annotation.Resource;

import org.docksidestage.app.web.base.HangarBaseAction;
import org.docksidestage.dbflute.allcommon.CDef;
import org.docksidestage.dbflute.exbhv.MemberAddressBhv;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exbhv.MemberSecurityBhv;
import org.docksidestage.dbflute.exbhv.MemberServiceBhv;
import org.docksidestage.dbflute.exbhv.MemberStatusBhv;
import org.docksidestage.dbflute.exbhv.ServiceRankBhv;
import org.docksidestage.mylasta.action.HangarUserBean;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author shunsuke.tadokoro
 */
public class MypageAction extends HangarBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // -----------------------------------------------------
    //                                          DI Component
    //                                          ------------
    @Resource
    private MemberBhv memberBhv;
    @Resource
    private MemberStatusBhv memberStatusBhv;
    @Resource
    private MemberSecurityBhv memberSecurityBhv;
    @Resource
    private MemberServiceBhv memberServiceBhv;
    @Resource
    private ServiceRankBhv serviceRankBhv;
    @Resource
    private MemberAddressBhv memberAddressBhv;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<MypageBean> index() {
        HangarUserBean userBean = getUserBean().get();
        MypageBean bean = new MypageBean();
        bean.setMemberId(new Integer(userBean.getMemberId().toString()));
        bean.setMemberName(userBean.getMemberName());
        bean.setMemberStatusCode(selectMemberStatusCode(bean.memberId));
        bean.setMemberServiceName(selectMemberServiseRankNameFromMemberId(bean.memberId));
        bean.setMemberPassword(selectMemberPassword(bean.memberId));
        bean.setMemberAddress(selectMemberAddress(bean.memberId));
        return asJson(bean);
    }

    // ===================================================================================
    //                                                                              Select
    //                                                                              ======
    private String selectMemberStatusCode(Integer memberId) {
        return memberBhv.selectEntity(cb -> {
            cb.specify().columnMemberStatusCode();
            cb.query().setMemberId_Equal(memberId);
        }).map(member -> {
            return member.getMemberStatusCode();
        }).get();
    }

    private CDef.ServiceRank selectMemberServiceRankCode(Integer memberId) {
        return memberServiceBhv.selectEntity(cb -> {
            cb.specify().columnServiceRankCode();
            cb.query().setMemberId_Equal(memberId);
        }).map(rankCode -> {
            return rankCode.getServiceRankCodeAsServiceRank();
        }).get();
    }

    private String selectMemberServiceRankName(CDef.ServiceRank rankCode) {
        return serviceRankBhv.selectEntity(cb -> {
            cb.specify().columnServiceRankName();
            cb.query().setServiceRankCode_Equal_AsServiceRank(rankCode);
        }).map(rank -> {
            return rank.getServiceRankName();
        }).get();
    }

    private String selectMemberPassword(Integer memberId) {
        return memberSecurityBhv.selectEntity(cb -> {
            cb.specify().columnLoginPassword();
            cb.query().setMemberId_Equal(memberId);
        }).map(security -> {
            return security.getLoginPassword().toString();
        }).orElse("");
    }

    private String selectMemberAddress(Integer memberId) {
        return memberAddressBhv.selectEntity(cb -> {
            cb.specify().columnAddress();
            cb.query().addOrderBy_RegisterDatetime_Desc();
            cb.query().setMemberId_Equal(memberId);
            cb.fetchFirst(1);
        }).map(address -> {
            return address.getAddress();
        }).orElse("");
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private String selectMemberServiseRankNameFromMemberId(Integer memberId) {
        return selectMemberServiceRankName(selectMemberServiceRankCode(memberId));
    }
}
