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
package org.docksidestage.app.web.signin;

import javax.annotation.Resource;

import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.docksidestage.app.web.base.login.ShowbaseLoginAssist;
import org.docksidestage.dbflute.exbhv.MemberLoginBhv;
import org.docksidestage.mylasta.action.ShowbaseMessages;
import org.docksidestage.unit.UnitShowbaseTestCase;
import org.lastaflute.core.time.TimeManager;
import org.lastaflute.web.response.JsonResponse;
import org.lastaflute.web.validation.Required;

/**
 * @author jflute
 */
public class SigninActionTest extends UnitShowbaseTestCase {

    @Resource
    private TimeManager timeManager;
    @Resource
    private MemberLoginBhv memberLoginBhv;
    @Resource
    private ShowbaseLoginAssist loginAssist;

    // ===================================================================================
    //                                                                               Basic
    //                                                                               =====
    public void test_index_basic() {
        // ## Arrange ##
        changeAsyncToNormalSync();
        changeRequiresNewToRequired();
        SigninAction action = new SigninAction();
        inject(action);
        SigninBody body = new SigninBody();
        body.account = "Pixy";
        body.password = "sea";

        // ## Act ##
        JsonResponse<SigninResult> response = action.post$index(body);

        // ## Assert ##
        TestingJsonData<SigninResult> jsonData = validateJsonData(response);
        SigninResult jsonResult = jsonData.getJsonResult();
        assertEquals(body.account, loginAssist.findByAuthToken(jsonResult.token).get().getMemberAccount());
        assertEquals(body.account, loginAssist.getSavedUserBean().get().getMemberAccount());
        assertEquals(1, memberLoginBhv.selectCount(cb -> {
            cb.query().setLoginDatetime_Equal(timeManager.currentDateTime()); // transaction time
        }));
    }

    // ===================================================================================
    //                                                                          Validation
    //                                                                          ==========
    public void test_index_validationError_required() {
        // ## Arrange ##
        SigninAction action = new SigninAction();
        inject(action);
        SigninBody body = new SigninBody();

        // ## Act ##
        assertValidationError(() -> action.post$index(body)).handle(data -> {
            // ## Assert ##
            log(ln() + data.requiredMessages().toDisp());
            data.requiredMessageOf("account", Required.class);
            data.requiredMessageOf("password", Required.class);
        });
    }

    public void test_index_validationError_loginFailure() {
        // ## Arrange ##
        SigninAction action = new SigninAction();
        inject(action);
        SigninBody body = new SigninBody();
        body.account = "Pixy";
        body.password = "land";

        // ## Act ##
        assertValidationError(() -> action.post$index(body)).handle(data -> {
            // ## Assert ##
            log(ln() + data.requiredMessages().toDisp());
            data.requiredMessageOf("account", ShowbaseMessages.ERRORS_LOGIN_FAILURE);
        });
    }
}
