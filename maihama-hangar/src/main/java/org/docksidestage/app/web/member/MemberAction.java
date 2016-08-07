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

import javax.annotation.Resource;

import org.docksidestage.app.web.base.HangarBaseAction;
import org.docksidestage.app.web.base.view.DisplayAssist;
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
    @Resource
    private DisplayAssist displayAssist;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<Void> update(MemberBody body) {
        validate(body, messages -> {});
        Member member = new Member();
        member.setMemberId(body.memberId);
        member.setMemberName(body.memberName);
        member.setBirthdate(displayAssist.toDate(body.birthdate).orElse(null));
        member.setMemberStatusCodeAsMemberStatus(body.memberStatusCode);
        member.setMemberAccount(body.memberAccount);
        memberBhv.update(member);
        return JsonResponse.asEmptyBody();
    }

    @Execute
    public JsonResponse<MemberInfoBean> info() {
        MemberInfoBean bean = new MemberInfoBean();
        getUserBean().ifPresent(userBean -> {
            bean.memberId = userBean.getMemberId();
            bean.memberName = userBean.getMemberName();
            bean.memberStatusName = selectMemberStatusName(userBean.getMemberId());
        }).orElse(() -> {
            bean.memberName = "Guest";
        });
        return asJson(bean);
    }

    // ===================================================================================
    //                                                                              Select
    //                                                                              ======
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
