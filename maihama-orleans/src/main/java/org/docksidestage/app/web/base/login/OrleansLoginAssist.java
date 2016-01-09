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
package org.docksidestage.app.web.base.login;

import javax.annotation.Resource;

import org.dbflute.optional.OptionalEntity;
import org.dbflute.optional.OptionalThing;
import org.docksidestage.app.web.signin.SigninAction;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exbhv.MemberLoginBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.docksidestage.dbflute.exentity.MemberLogin;
import org.docksidestage.mylasta.action.OrleansUserBean;
import org.docksidestage.mylasta.direction.OrleansConfig;
import org.lastaflute.core.magic.async.AsyncManager;
import org.lastaflute.core.time.TimeManager;
import org.lastaflute.db.jta.stage.TransactionStage;
import org.lastaflute.web.login.option.LoginSpecifiedOption;

/**
 * @author jflute
 */
public class OrleansLoginAssist extends MaihamaLoginAssist<OrleansUserBean, Member> { // #change_it

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private TimeManager timeManager;
    @Resource
    private AsyncManager asyncManager;
    @Resource
    private TransactionStage transactionStage;
    @Resource
    private OrleansConfig orleansConfig;
    @Resource
    private MemberBhv memberBhv;
    @Resource
    private MemberLoginBhv memberLoginBhv;

    // ===================================================================================
    //                                                                           Find User
    //                                                                           =========
    @Override
    protected boolean doCheckUserLoginable(String email, String cipheredPassword) {
        return memberBhv.selectCount(cb -> {
            cb.query().arrangeLogin(email, cipheredPassword);
        }) > 0;
    }

    @Override
    protected OptionalEntity<Member> doFindLoginUser(String email, String cipheredPassword) {
        return memberBhv.selectEntity(cb -> {
            cb.query().arrangeLogin(email, cipheredPassword);
        });
    }

    @Override
    protected OptionalEntity<Member> doFindLoginUser(Integer userId) {
        return memberBhv.selectEntity(cb -> {
            cb.query().arrangeLoginByIdentity(userId);
        });
    }

    // ===================================================================================
    //                                                                       Login Process
    //                                                                       =============
    @Override
    protected OrleansUserBean createUserBean(Member userEntity) {
        return new OrleansUserBean(userEntity);
    }

    @Override
    protected OptionalThing<String> getCookieRememberMeKey() {
        return OptionalThing.of(orleansConfig.getCookieRememberMeOrleansKey());
    }

    @Override
    protected void saveLoginHistory(Member member, OrleansUserBean userBean, LoginSpecifiedOption option) {
        asyncManager.async(() -> {
            transactionStage.requiresNew(tx -> {
                insertLogin(member);
            });
        });
    }

    protected void insertLogin(Member member) {
        MemberLogin login = new MemberLogin();
        login.setMemberId(member.getMemberId());
        login.setLoginMemberStatusCodeAsMemberStatus(member.getMemberStatusCodeAsMemberStatus());
        login.setLoginDatetime(timeManager.currentDateTime());
        login.setMobileLoginFlg_False(); // mobile unsupported for now
        memberLoginBhv.insert(login);
    }

    // ===================================================================================
    //                                                                      Login Resource
    //                                                                      ==============
    @Override
    protected Class<OrleansUserBean> getUserBeanType() {
        return OrleansUserBean.class;
    }

    @Override
    protected Class<?> getLoginActionType() {
        return SigninAction.class;
    }
}
