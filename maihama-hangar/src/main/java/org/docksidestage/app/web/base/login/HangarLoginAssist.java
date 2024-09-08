/*
 * Copyright 2015-2024 the original author or authors.
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
import org.dbflute.util.DfStringUtil;
import org.docksidestage.app.web.signin.SigninAction;
import org.docksidestage.dbflute.cbean.MemberCB;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exbhv.MemberLoginBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.docksidestage.dbflute.exentity.MemberLogin;
import org.docksidestage.mylasta.action.HangarUserBean;
import org.docksidestage.mylasta.direction.HangarConfig;
import org.lastaflute.core.magic.async.AsyncManager;
import org.lastaflute.core.time.TimeManager;
import org.lastaflute.db.jta.stage.TransactionStage;
import org.lastaflute.web.login.LoginHandlingResource;
import org.lastaflute.web.login.PrimaryLoginManager;
import org.lastaflute.web.login.credential.UserPasswordCredential;
import org.lastaflute.web.login.option.LoginSpecifiedOption;
import org.lastaflute.web.servlet.cookie.CookieCipher;
import org.lastaflute.web.servlet.cookie.exception.CookieCipherDecryptFailureException;
import org.lastaflute.web.servlet.request.RequestManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jflute
 * @author black-trooper
 */
public class HangarLoginAssist extends MaihamaLoginAssist<HangarUserBean, Member> // #change_it also UserBean
        implements PrimaryLoginManager { // #app_customize

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final Logger logger = LoggerFactory.getLogger(HangarLoginAssist.class);
    private static final String DELIMITER = ":";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private TimeManager timeManager;
    @Resource
    private AsyncManager asyncManager;
    @Resource
    private RequestManager requestManager;
    @Resource
    private TransactionStage transactionStage;
    @Resource
    private HangarConfig config;
    @Resource
    private MemberBhv memberBhv;
    @Resource
    private MemberLoginBhv memberLoginBhv;
    @Resource
    private CookieCipher cookieCipher;

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
        return memberBhv.selectEntity(cb -> {
            cb.query().arrangeLoginByIdentity(userId);
        });
    }

    // ===================================================================================
    //                                                                       Login Process
    //                                                                       =============
    @Override
    protected HangarUserBean createUserBean(Member userEntity) {
        return new HangarUserBean(userEntity);
    }

    @Override
    protected OptionalThing<String> getCookieRememberMeKey() {
        return OptionalThing.of(config.getCookieRememberMeHangarKey());
    }

    @Override
    protected void saveLoginHistory(Member member, HangarUserBean userBean, LoginSpecifiedOption option) {
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
    // #app_customize like this if your use header auth token
    @Override
    protected boolean tryAlreadyLoginOrRememberMe(LoginHandlingResource resource) {
        if (super.tryAlreadyLoginOrRememberMe(resource)) {
            return true;
        }
        return requestManager.getHeader("x-authorization").flatMap(token -> {
            final String decrypted;
            try {
                decrypted = cookieCipher.decrypt(token);
            } catch (CookieCipherDecryptFailureException e) {
                logger.info("*Failed to decrypt the requested auth_key so cannot login: {}", e.getMessage());
                return null;
            }
            return findByAuthToken(decrypted).map(member -> {
                saveLoginInfoToSession(member);
                return true;
            });
        }).orElse(false);
    }

    private OptionalEntity<Member> findByAuthToken(String token) {
        final String account = DfStringUtil.substringFirstFront(token, DELIMITER);
        final String password = DfStringUtil.substringFirstRear(token, DELIMITER);
        return memberBhv.selectEntity(cb -> {
            cb.query().arrangeLogin(account, encryptPassword(password));
        });
    }

    // ===================================================================================
    //                                                                      Login Resource
    //                                                                      ==============
    @Override
    protected Class<HangarUserBean> getUserBeanType() {
        return HangarUserBean.class;
    }

    @Override
    protected Class<?> getLoginActionType() {
        return SigninAction.class;
    }

    public String generateAuthKey(String account, String password) {
        return cookieCipher.encrypt(account + DELIMITER + password);
    }
}
