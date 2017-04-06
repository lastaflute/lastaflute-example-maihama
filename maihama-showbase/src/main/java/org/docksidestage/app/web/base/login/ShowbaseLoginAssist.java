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
package org.docksidestage.app.web.base.login;

import org.dbflute.optional.OptionalEntity;
import org.dbflute.optional.OptionalThing;
import org.docksidestage.dbflute.exentity.Member;
import org.docksidestage.mylasta.action.ShowbaseUserBean;
import org.lastaflute.web.login.option.LoginSpecifiedOption;

/**
 * @author jflute
 */
public class ShowbaseLoginAssist extends MaihamaLoginAssist<ShowbaseUserBean, Member> { // #change_it

    // ===================================================================================
    //                                                                           Find User
    //                                                                           =========
    @Override
    protected void checkCredential(CredentialChecker checker) {
        throw new UnsupportedOperationException("no login in this project");
    }

    @Override
    protected void resolveCredential(CredentialResolver resolver) {
        throw new UnsupportedOperationException("no login in this project");
    }

    @Override
    protected OptionalEntity<Member> doFindLoginUser(Integer userId) {
        throw new UnsupportedOperationException("no login in this project");
    }

    // ===================================================================================
    //                                                                       Login Process
    //                                                                       =============
    @Override
    protected ShowbaseUserBean createUserBean(Member userEntity) {
        throw new UnsupportedOperationException("no login in this project");
    }

    @Override
    protected OptionalThing<String> getCookieRememberMeKey() {
        throw new UnsupportedOperationException("no login in this project");
    }

    @Override
    protected void saveLoginHistory(Member member, ShowbaseUserBean userBean, LoginSpecifiedOption option) {
        throw new UnsupportedOperationException("no login in this project");
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
        throw new UnsupportedOperationException("no login in this project");
    }
}
