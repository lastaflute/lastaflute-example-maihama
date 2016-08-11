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
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.lastaflute.core.time.TimeManager;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author shunsuke.tadokoro
 * @author jflute
 */
public class MypageAction extends HangarBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private TimeManager timeManager;
    @Resource
    private MemberBhv memberBhv;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<MypageBean> index() {
        Integer memberId = getUserBean().get().getMemberId();
        Member member = selectMember(memberId);
        MypageBean bean = mappingToBean(memberId, member);
        return asJson(bean);
    }

    // ===================================================================================
    //                                                                              Select
    //                                                                              ======
    private Member selectMember(Integer memberId) {
        return memberBhv.selectEntity(cb -> {
            cb.setupSelect_MemberAddressAsValid(timeManager.currentDate());
            cb.setupSelect_MemberSecurityAsOne();
            cb.setupSelect_MemberServiceAsOne().withServiceRank();
            cb.query().setMemberId_Equal(memberId);
        }).get();
    }

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    private MypageBean mappingToBean(Integer memberId, Member member) {
        MypageBean bean = new MypageBean();
        bean.memberId = memberId;
        bean.memberName = member.getMemberName();
        bean.memberStatus = member.getMemberStatusCodeAsMemberStatus().alias();
        bean.serviceRank = member.getMemberServiceAsOne().get().getServiceRank().get().getServiceRankName();
        bean.cipheredPassword = member.getMemberSecurityAsOne().get().getLoginPassword();
        bean.memberAddress = member.getMemberAddressAsValid().map(address -> address.getAddress()).orElse(null);
        return bean;
    }
}
