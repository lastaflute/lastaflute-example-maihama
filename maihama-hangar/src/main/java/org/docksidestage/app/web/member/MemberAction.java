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

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.docksidestage.app.web.base.HangarBaseAction;
import org.docksidestage.dbflute.allcommon.CDef.MemberStatus;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author iwamatsu0430
 * @author jflute
 * @author black-trooper
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

    @Execute
    public JsonResponse<List<SimpleEntry<String, String>>> status() {
        List<SimpleEntry<String, String>> memberStatusList = MemberStatus.listAll().stream().map(m -> {
            return new SimpleEntry<>(m.code(), m.alias());
        }).collect(Collectors.toList());
        return asJson(memberStatusList);
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
