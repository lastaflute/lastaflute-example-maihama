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
package org.docksidestage.app.web;

import java.util.Map;

import javax.annotation.Resource;

import org.docksidestage.app.web.base.ShowbaseBaseAction;
import org.docksidestage.mylasta.direction.ShowbaseConfig;
import org.docksidestage.mylasta.direction.sponsor.ShowbaseApiFailureHook;
import org.lastaflute.meta.SwaggerGenerator;
import org.lastaflute.meta.SwaggerOption.SwaggerFailureHttpStatusResource;
import org.lastaflute.meta.agent.SwaggerAgent;
import org.lastaflute.meta.swagger.web.LaActionSwaggerable;
import org.lastaflute.web.Execute;
import org.lastaflute.web.login.AllowAnyoneAccess;
import org.lastaflute.web.response.HtmlResponse;
import org.lastaflute.web.response.JsonResponse;
import org.lastaflute.web.ruts.config.restful.httpstatus.TypicalStructuredSuccessHttpStatusHandler;
import org.lastaflute.web.servlet.request.RequestManager;

/**
 * The action to show swaggar-ui.
 * @author awaawa
 * @author jflute
 */
@AllowAnyoneAccess
public class SwaggerAction extends ShowbaseBaseAction implements LaActionSwaggerable {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private RequestManager requestManager;
    @Resource
    private ShowbaseConfig config;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public HtmlResponse index() {
        verifySwaggerAllowed();
        String swaggerJsonUrl = toActionUrl(SwaggerAction.class, moreUrl("json"));
        return new SwaggerAgent(requestManager).prepareSwaggerUiResponse(swaggerJsonUrl);
    }

    @Execute
    public JsonResponse<Map<String, Object>> json() {
        verifySwaggerAllowed();
        Map<String, Object> swaggerMap = new SwaggerGenerator().generateSwaggerMap(op -> {
            op.derivedSuccessHttpStatus(meta -> {
                // conventional success status according to action structure
                //  e.g. post: 201, put/delete withtout return: 204
                TypicalStructuredSuccessHttpStatusHandler statusHandler = new TypicalStructuredSuccessHttpStatusHandler();
                return statusHandler.deriveSuccessStatus(meta.getActionExecute()).orElse(null);
            });
            op.derivedFailureHttpStatus(meta -> {
                // synchronize with failure hook's mapping (exception to status)
                //  e.g. EntityAlreadyDeletedException: 404
                SwaggerFailureHttpStatusResource resource = new SwaggerFailureHttpStatusResource();
                resource.acceptFailureStatusMap(new ShowbaseApiFailureHook().getBusinessHttpStatusMapping());
                return resource;
            });
        });
        return asJson(swaggerMap).switchMappingOption(op -> {}); // not to depend on application settings
    }

    private void verifySwaggerAllowed() { // also check in ActionAdjustmentProvider
        verifyOrClientError("Swagger is not enabled.", config.isSwaggerEnabled());
    }
}
