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
package org.docksidestage.app.web;

import java.util.Map;

import javax.annotation.Resource;

import org.docksidestage.app.web.base.ShowbaseBaseAction;
import org.docksidestage.mylasta.direction.ShowbaseConfig;
import org.lastaflute.doc.SwaggerGenerator;
import org.lastaflute.doc.agent.SwaggerAgent;
import org.lastaflute.web.Execute;
import org.lastaflute.web.login.AllowAnyoneAccess;
import org.lastaflute.web.response.HtmlResponse;
import org.lastaflute.web.response.JsonResponse;
import org.lastaflute.web.servlet.request.RequestManager;

/**
 * @author awaawa
 * @author jflute
 */
@AllowAnyoneAccess
public class SwaggerAction extends ShowbaseBaseAction {

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
        return new SwaggerAgent(requestManager).prepareSwaggerUiResponse("/swagger/json/");
    }

    @Execute
    public JsonResponse<Map<String, Object>> json() {
        verifySwaggerAllowed();
        return asJson(new SwaggerGenerator().generateSwaggerMap());
    }

    private void verifySwaggerAllowed() {
        verifyOrClientError("Swagger is in development environment only", config.isDevelopmentHere());
    }
}
