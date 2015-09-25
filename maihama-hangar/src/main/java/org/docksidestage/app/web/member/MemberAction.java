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

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.dbflute.cbean.result.ListResultBean;
import org.docksidestage.app.web.base.HangarBaseAction;
import org.docksidestage.dbflute.allcommon.CDef;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exbhv.MemberStatusBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.docksidestage.dbflute.exentity.MemberStatus;
import org.lastaflute.web.Execute;
import org.lastaflute.web.callback.ActionRuntime;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author iwamatsu0430
 */
public class MemberAction extends HangarBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    protected MemberBhv memberBhv;
    @Resource
    protected MemberStatusBhv memberStatusBhv;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<Void> update(MemberBody body) {
        validate(body, messages -> {});
        Member member = new Member();
        member.setMemberId(body.memberId);
        member.setMemberName(body.memberName);
        member.setBirthdate(toDate(body.birthdate).orElse(null));
        member.setMemberStatusCodeAsMemberStatus(toCls(CDef.MemberStatus.class, body.memberStatusCode).get());
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
    protected Member selectMember(Integer memberId) {
        return memberBhv.selectEntity(cb -> {
            cb.specify().derivedMemberLogin().max(loginCB -> {
                loginCB.specify().columnLoginDatetime();
            } , Member.ALIAS_latestLoginDatetime);
            cb.query().setMemberId_Equal(memberId);
            cb.query().setMemberStatusCode_InScope_ServiceAvailable();
        }).get();
    }

    protected Map<String, String> prepareMemberStatusMap() {
        ListResultBean<MemberStatus> statusList = memberStatusBhv.selectList(cb -> {
            cb.query().addOrderBy_DisplayOrder_Asc();
        });
        Map<String, String> statusMap = new LinkedHashMap<String, String>();
        statusList.forEach(status -> {
            statusMap.put(status.getMemberStatusCode(), status.getMemberStatusName());
        });
        return statusMap;
    }

    protected String selectMemberStatusCode(Integer memberId) {
        return memberBhv.selectEntity(cb -> {
            cb.specify().columnMemberStatusCode();
            cb.query().setMemberId_Equal(memberId);
        }).map(member -> {
            return member.getMemberStatusCode();
        }).orElse("");
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

    // ===================================================================================
    //                                                                            Callback
    //                                                                            ========
    @Override
    public void hookFinally(ActionRuntime runtime) {
        if (runtime.isForwardToHtml()) {
            prepareListBox(runtime); // 会員ステータスなどリストボックスの構築
        }
        super.hookFinally(runtime);
    }

    // ===================================================================================
    //                                                                               Logic
    //                                                                               =====
    protected void prepareListBox(ActionRuntime runtime) { // ここはアプリによって色々かと by jflute
        ListResultBean<MemberStatus> statusList = memberStatusBhv.selectList(cb -> {
            cb.query().addOrderBy_DisplayOrder_Asc();
        });
        Map<String, String> statusMap = new LinkedHashMap<String, String>();
        statusList.forEach(status -> statusMap.put(status.getMemberStatusCode(), status.getMemberStatusName()));
        runtime.registerData("memberStatusMap", statusMap);
    }
}
