/*
 * Copyright 2015-2022 the original author or authors.
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

import org.dbflute.optional.OptionalEntity;
import org.dbflute.optional.OptionalThing;
import org.docksidestage.app.web.signin.SigninAction;
import org.docksidestage.dbflute.cbean.MemberCB;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exbhv.MemberLoginBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.docksidestage.dbflute.exentity.MemberLogin;
import org.docksidestage.mylasta.action.ShowbaseUserBean;
import org.docksidestage.mylasta.direction.ShowbaseConfig;
import org.lastaflute.core.magic.async.AsyncManager;
import org.lastaflute.core.time.TimeManager;
import org.lastaflute.db.jta.stage.TransactionStage;
import org.lastaflute.web.login.LoginHandlingResource;
import org.lastaflute.web.login.PrimaryLoginManager;
import org.lastaflute.web.login.credential.UserPasswordCredential;
import org.lastaflute.web.login.option.LoginSpecifiedOption;
import org.lastaflute.web.servlet.request.RequestManager;

import jakarta.annotation.Resource;

/**
 * @author jflute
 */
public class ShowbaseLoginAssist extends MaihamaLoginAssist<ShowbaseUserBean, Member> // #change_it also UserBean
        implements PrimaryLoginManager { // #app_customize

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
    private RequestManager requestManager;
    @Resource
    private ShowbaseConfig config;
    @Resource
    private MemberBhv memberBhv;
    @Resource
    private MemberLoginBhv memberLoginBhv;
    @Resource
    private AuthTokenAssist authTokenAssist;

    // ===================================================================================
    //                                                                           Find User
    //                                                                           =========
    @Override
    protected void checkCredential(CredentialChecker checker) {
        checker.check(UserPasswordCredential.class, credential -> {
            return memberBhv.selectCount(cb -> arrangeLoginByCredential(cb, credential)) > 0;
        });
    }

    @Override
    protected void resolveCredential(CredentialResolver resolver) {
        resolver.resolve(UserPasswordCredential.class, credential -> {
            return memberBhv.selectEntity(cb -> arrangeLoginByCredential(cb, credential));
        });
    }

    private void arrangeLoginByCredential(MemberCB cb, UserPasswordCredential credential) {
        cb.query().arrangeLogin(credential.getUser(), encryptPassword(credential.getPassword()));
    }

    @Override
    protected OptionalEntity<Member> doFindLoginUser(Integer userId) {
        return memberBhv.selectEntity(cb -> cb.query().arrangeLoginByIdentity(userId));
    }

    // ===================================================================================
    //                                                                       Login Process
    //                                                                       =============
    @Override
    protected ShowbaseUserBean createUserBean(Member userEntity) {
        return new ShowbaseUserBean(userEntity);
    }

    @Override
    protected OptionalThing<String> getCookieRememberMeKey() { // #app_customize empty if completely no remember-me
        return OptionalThing.of(config.getCookieRememberMeShowbaseKey()); // if hybrid with cookie
    }

    @Override
    protected Integer toTypedUserId(String userKey) {
        return Integer.valueOf(userKey);
    }

    @Override
    protected void saveLoginHistory(Member member, ShowbaseUserBean userBean, LoginSpecifiedOption option) {
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
    //                                                                         Login Check
    //                                                                         ===========
    @Override
    protected boolean tryAlreadyLoginOrRememberMe(LoginHandlingResource resource) {
        if (super.tryAlreadyLoginOrRememberMe(resource)) { // if hybrid with session
            return true;
        }
        return requestManager.getHeader("x-authorization").flatMap(token -> {
            return findByAuthToken(token).map(member -> {
                syncCheckLoginSessionIfNeeds(createUserBean(member)); // sometimes synchronize with database
                saveLoginInfoToSession(member); // if hybrid with session
                return true;
            });
        }).orElse(false);
    }

    // ===================================================================================
    //                                                                 Authorization Token
    //                                                                 ===================
    public String saveAuthToken() { // basically for signin
        return authTokenAssist.saveMemberToken(getSavedUserBean().get().getMemberAccount());
    }

    public OptionalThing<Member> findByAuthToken(String token) {
        return authTokenAssist.findMemberByToken(token);
    }

    // ===================================================================================
    //                                                                      Login Resource
    //                                                                      ==============
    @Override
    protected Class<ShowbaseUserBean> getUserBeanType() {
        return ShowbaseUserBean.class;
    }

    @Override
    protected Class<?> getLoginActionType() {
        return SigninAction.class;
    }
}
