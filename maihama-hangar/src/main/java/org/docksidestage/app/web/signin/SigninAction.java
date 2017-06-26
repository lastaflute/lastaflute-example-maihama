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
package org.docksidestage.app.web.signin;

import javax.annotation.Resource;

import org.docksidestage.app.web.base.HangarBaseAction;
import org.docksidestage.app.web.base.login.HangarLoginAssist;
import org.docksidestage.mylasta.action.HangarMessages;
import org.lastaflute.core.util.LaStringUtil;
import org.lastaflute.web.Execute;
import org.lastaflute.web.login.credential.UserPasswordCredential;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author iwamatsu0430
 * @author jflute
 * @author black-trooper
 */
public class SigninAction extends HangarBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private HangarLoginAssist loginAssist;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<String> index(SigninBody body) {
        validate(body, messages -> moreValidate(body, messages));
        boolean rememberMe = false; // #simple_for_example no remember for now
        loginAssist.login(createCredential(body), op -> op.rememberMe(rememberMe));
        return asJson(loginAssist.generateAuthKey(body.account, body.password));
    }

    private void moreValidate(SigninBody body, HangarMessages messages) {
        if (LaStringUtil.isNotEmpty(body.account) && LaStringUtil.isNotEmpty(body.password)) {
            if (!loginAssist.checkUserLoginable(createCredential(body))) {
                messages.addErrorsLoginFailure("account");
            }
        }
    }

    private UserPasswordCredential createCredential(SigninBody body) {
        return new UserPasswordCredential(body.account, body.password);
    }
}
