/*
 * Copyright 2015-2017 the original author or authors.
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

import org.docksidestage.app.web.base.ShowbaseBaseAction;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.lastaflute.core.time.TimeManager;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author jflute
 */
public class MypageAction extends ShowbaseBaseAction {

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
    public JsonResponse<MypageResult> get$index() {
        Integer memberId = getUserBean().get().getMemberId();
        Member member = selectMember(memberId);
        MypageResult result = mappingToResult(memberId, member);
        return asJson(result);
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
    private MypageResult mappingToResult(Integer memberId, Member member) {
        MypageResult result = new MypageResult();
        result.memberId = memberId;
        result.memberName = member.getMemberName();
        result.memberStatus = member.getMemberStatusCodeAsMemberStatus().alias();
        result.serviceRank = member.getMemberServiceAsOne().get().getServiceRank().get().getServiceRankName();
        result.cipheredPassword = member.getMemberSecurityAsOne().get().getLoginPassword();
        result.memberAddress = member.getMemberAddressAsValid().map(address -> address.getAddress()).orElse(null);
        return result;
    }
}
