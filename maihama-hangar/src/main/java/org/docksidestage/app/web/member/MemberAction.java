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
package org.docksidestage.app.web.member;

import javax.annotation.Resource;

import org.dbflute.optional.OptionalEntity;
import org.docksidestage.app.web.base.HangarBaseAction;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author iwamatsu0430
 * @author jflute
 */
public class MemberAction extends HangarBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private MemberBhv memberBhv;

    // #pending review later
    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<MemberBody> detail(Integer memberId) {
        MemberBody memberBody = new MemberBody();
        selectMember(memberId).alwaysPresent(member -> {
            memberBody.memberId = member.getMemberId();
            memberBody.memberName = member.getMemberName();
            memberBody.memberStatusCode = member.getMemberStatusCodeAsMemberStatus();
            memberBody.memberAccount = member.getMemberAccount();
        });
        return asJson(memberBody);
    }

    @Execute
    public JsonResponse<Void> update(MemberBody body) {
        validate(body, messages -> {});
        Member member = new Member();
        member.setMemberId(body.memberId);
        member.setMemberName(body.memberName);
        member.setBirthdate(body.birthdate);
        member.setMemberStatusCodeAsMemberStatus(body.memberStatusCode);
        member.setMemberAccount(body.memberAccount);
        memberBhv.update(member);
        return JsonResponse.asEmptyBody();
    }

    @Execute
    public JsonResponse<MemberInfoResult> info() {
        MemberInfoResult result = new MemberInfoResult();
        getUserBean().ifPresent(userBean -> {
            result.memberId = userBean.getMemberId();
            result.memberName = userBean.getMemberName();
            result.memberStatusName = selectMemberStatusName(userBean.getMemberId());
        }).orElse(() -> {
            result.memberName = "Guest";
        });
        return asJson(result);
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

    private String selectMemberStatusName(Integer memberId) {
        return memberBhv.selectEntity(cb -> {
            cb.setupSelect_MemberStatus();
            cb.specify().specifyMemberStatus().columnMemberStatusName();
            cb.query().setMemberId_Equal(memberId);
        }).flatMap(member -> {
            return member.getMemberStatus().map(status -> {
                return status.getMemberStatusName();
            });
        }).orElse("");
    }
}
