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

import org.docksidestage.app.web.base.HangarBaseAction;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.lastaflute.core.time.TimeManager;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author jflute
 * @author black-trooper
 */
public class MemberAddAction extends HangarBaseAction {

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
    public JsonResponse<Member> register(MemberAddBody body) {
        validate(body, messages -> {});
        Member member = insertMember(body);
        return asJson(member);
    }

    // ===================================================================================
    //                                                                              Update
    //                                                                              ======
    private Member insertMember(MemberAddBody body) {
        Member member = new Member();
        member.setMemberName(body.memberName);
        member.setMemberAccount(body.memberAccount);
        member.setBirthdate(body.birthdate);
        member.setMemberStatusCodeAsMemberStatus(body.memberStatus);
        if (member.isMemberStatusCodeFormalized()) {
            member.setFormalizedDatetime(timeManager.currentDateTime());
        }
        memberBhv.insert(member);
        return member;
    }
}
