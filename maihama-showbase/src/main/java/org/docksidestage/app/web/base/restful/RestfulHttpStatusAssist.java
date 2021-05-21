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
package org.docksidestage.app.web.base.restful;

import javax.servlet.http.HttpServletResponse;

import org.dbflute.optional.OptionalThing;
import org.lastaflute.web.response.ActionResponse;
import org.lastaflute.web.ruts.config.ActionExecute;
import org.lastaflute.web.ruts.process.ActionRuntime;

/**
 * @author jflute
 */
public class RestfulHttpStatusAssist { // #app_customize

    // ===================================================================================
    //                                                                              Derive
    //                                                                              ======
    public OptionalThing<Integer> deriveConventionalHttpStatus(ActionRuntime runtime) {
        if (!canOverrideSpecifiedHttpStatus(runtime) && hasAlreadyHttpStatus(runtime)) {
            return OptionalThing.empty();
        }
        Integer httpStatus = null;
        if (runtime.hasActionResponse() && withoutFailureAndError(runtime)) { // precondition
            if (isCreatedTargetMethod(runtime)) {
                httpStatus = HttpServletResponse.SC_CREATED;
            } else if (isNoContentTargetMethod(runtime) && isReturnAsEmptyBody(runtime)) {
                httpStatus = HttpServletResponse.SC_NO_CONTENT;
            }
        }
        return OptionalThing.ofNullable(httpStatus, () -> {
            throw new IllegalStateException("Not found the conventional HTTP status: " + runtime);
        });
    }

    // ===================================================================================
    //                                                                     Override Option
    //                                                                     ===============
    protected boolean canOverrideSpecifiedHttpStatus(ActionRuntime runtime) { // you can select it
        return false; // non-override as default
    }

    protected boolean hasAlreadyHttpStatus(ActionRuntime runtime) {
        ActionResponse response = runtime.getActionResponse(); // null allowed
        return response != null && response.getHttpStatus().isPresent();
    }

    // ===================================================================================
    //                                                                         HTTP Method
    //                                                                         ===========
    protected boolean isCreatedTargetMethod(ActionRuntime runtime) {
        return judgePostMethod(runtime);
    }

    protected boolean isNoContentTargetMethod(ActionRuntime runtime) {
        return judgePutMethod(runtime) || judgeDeleteMethod(runtime) || judgePatchMethod(runtime);
    }

    protected boolean judgePostMethod(ActionRuntime runtime) {
        return doJudgeHttpMethod(runtime, "post");
    }

    protected boolean judgePutMethod(ActionRuntime runtime) {
        return doJudgeHttpMethod(runtime, "put");
    }

    protected boolean judgePatchMethod(ActionRuntime runtime) {
        return doJudgeHttpMethod(runtime, "patch");
    }

    protected boolean judgeDeleteMethod(ActionRuntime runtime) {
        return doJudgeHttpMethod(runtime, "delete");
    }

    protected boolean doJudgeHttpMethod(ActionRuntime runtime, String httpMethod) {
        ActionExecute execute = runtime.getActionExecute();
        return execute.getRestfulHttpMethod().filter(mt -> mt.equalsIgnoreCase(httpMethod)).isPresent();
    }

    // ===================================================================================
    //                                                                     Action Response
    //                                                                     ===============
    protected boolean withoutFailureAndError(ActionRuntime runtime) {
        return runtime.withoutFailureAndError();
    }

    protected boolean isReturnAsEmptyBody(ActionRuntime runtime) {
        final ActionResponse response = runtime.getActionResponse(); // null allowed
        return response != null && response.isReturnAsEmptyBody();
    }
}
