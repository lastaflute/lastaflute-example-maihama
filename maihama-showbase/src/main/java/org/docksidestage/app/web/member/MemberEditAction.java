/*
 * Copyright 2015-2021 the original author or authors.
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

import javax.annotation.Resource;

import org.dbflute.optional.OptionalEntity;
import org.docksidestage.app.web.base.ShowbaseBaseAction;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author iwamatsu0430
 * @author jflute
 * @author black-trooper
 */
public class MemberEditAction extends ShowbaseBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private MemberBhv memberBhv;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<MemberEditBody> index(Integer memberId) {
        MemberEditBody memberEditBody = new MemberEditBody();
        selectMember(memberId).alwaysPresent(member -> {
            memberEditBody.memberId = member.getMemberId();
            memberEditBody.memberName = member.getMemberName();
            memberEditBody.birthdate = member.getBirthdate();
            memberEditBody.memberStatus = member.getMemberStatusCodeAsMemberStatus();
            memberEditBody.memberAccount = member.getMemberAccount();
            memberEditBody.versionNo = member.getVersionNo();
        });
        return asJson(memberEditBody);
    }

    @Execute
    public JsonResponse<Void> update(MemberEditBody body) {
        validate(body, messages -> {});
        Member member = new Member();
        member.setMemberId(body.memberId);
        member.setMemberName(body.memberName);
        member.setBirthdate(body.birthdate);
        member.setMemberStatusCodeAsMemberStatus(body.memberStatus);
        member.setMemberAccount(body.memberAccount);
        member.setVersionNo(body.versionNo);
        memberBhv.update(member);
        return JsonResponse.asEmptyBody();
    }

    // ===================================================================================
    //                                                                              Select
    //                                                                              ======
    private OptionalEntity<Member> selectMember(Integer memberId) {
        return memberBhv.selectEntity(cb -> {
            cb.specify().derivedMemberLogin().max(loginCB -> {
                loginCB.specify().columnLoginDatetime();
            }, Member.ALIAS_latestLoginDatetime);
            cb.query().setMemberId_Equal(memberId);
            cb.query().setMemberStatusCode_InScope_ServiceAvailable();
        });
    }
}
