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
package org.docksidestage.mylasta.direction.sponsor;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.dbflute.optional.OptionalThing;
import org.docksidestage.mylasta.action.ShowbaseMessages;
import org.lastaflute.web.api.ApiFailureResource;
import org.lastaflute.web.api.BusinessFailureMapping;
import org.lastaflute.web.api.theme.FaihyUnifiedFailureResult;
import org.lastaflute.web.api.theme.TypicalFaihyApiFailureHook;
import org.lastaflute.web.login.exception.LoginUnauthorizedException;

/**
 * @author jflute
 */
public class ShowbaseApiFailureHook extends TypicalFaihyApiFailureHook {

    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // [Reference Site]
    // http://dbflute.seasar.org/ja/lastaflute/howto/impldesign/jsonfaihy.html
    // _/_/_/_/_/_/_/_/_/_/

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final BusinessFailureMapping<Integer> httpStatusMapping; // for application exception

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public ShowbaseApiFailureHook() {
        httpStatusMapping = new BusinessFailureMapping<Integer>(failureMap -> {
            setupHttpStatusMap(failureMap);
        });
    }

    // -----------------------------------------------------
    //                                   Failure HTTP Status
    //                                   -------------------
    protected void setupHttpStatusMap(Map<Class<?>, Integer> failureMap) {
        // you can add mapping of failure status with exception here
        failureMap.put(LoginUnauthorizedException.class, HttpServletResponse.SC_UNAUTHORIZED);
    }

    // ===================================================================================
    //                                                                    Business Failure
    //                                                                    ================
    @Override
    protected int prepareBusinessFailureStatus(FaihyUnifiedFailureResult result, ApiFailureResource resource,
            OptionalThing<RuntimeException> optCause) {
        return optCause.flatMap(cause -> {
            return httpStatusMapping.findAssignable(cause);
        }).orElseGet(() -> {
            return super.prepareBusinessFailureStatus(result, resource, optCause);
        });
    }

    // ===================================================================================
    //                                                                          JSON Logic
    //                                                                          ==========
    @Override
    protected String getErrorsLoginRequired() {
        return ShowbaseMessages.ERRORS_LOGIN_REQUIRED;
    }

    @Override
    protected String getErrorsUnknownBusinessError() {
        return ShowbaseMessages.ERRORS_UNKNOWN_BUSINESS_ERROR;
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public BusinessFailureMapping<Integer> getHttpStatusMapping() { // for e.g. swagger
        return httpStatusMapping;
    }
}
