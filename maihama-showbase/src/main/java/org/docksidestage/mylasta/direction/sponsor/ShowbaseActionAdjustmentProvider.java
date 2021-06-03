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

import javax.servlet.http.HttpServletRequest;

import org.docksidestage.mylasta.direction.ShowbaseConfig;
import org.docksidestage.mylasta.direction.sponsor.planner.ShowbaseActionOptionAgent;
import org.lastaflute.web.path.UrlMappingOption;
import org.lastaflute.web.path.UrlMappingResource;
import org.lastaflute.web.path.UrlReverseOption;
import org.lastaflute.web.path.UrlReverseResource;
import org.lastaflute.web.path.restful.router.RestfulRouter;

/**
 * @author jflute
 */
public class ShowbaseActionAdjustmentProvider extends MaihamaActionAdjustmentProvider {

    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // if the following settings is used in other applications, migrate them to super class
    // _/_/_/_/_/_/_/_/_/_/

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final ShowbaseActionOptionAgent actionOptionAgent;

    // -----------------------------------------------------
    //                                         Cached Option
    //                                         -------------
    protected final RestfulRouter restfulRouter;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public ShowbaseActionAdjustmentProvider(ShowbaseConfig config) {
        this.actionOptionAgent = newActionOptionAgent(config);
        this.restfulRouter = actionOptionAgent.createRestfulRouter();
    }

    protected ShowbaseActionOptionAgent newActionOptionAgent(ShowbaseConfig config) {
        return new ShowbaseActionOptionAgent(config);
    }

    // ===================================================================================
    //                                                                             Routing
    //                                                                             =======
    // -----------------------------------------------------
    //                                 Typical Determination
    //                                 ---------------------
    @Override
    public boolean isForced404NotFoundRouting(HttpServletRequest request, String requestPath) {
        if (actionOptionAgent.isDisabledSwaggerRequest(requestPath)) { // e.g. swagger's html, css
            return true; // to suppress direct access to swagger resources at e.g. production
        }
        return super.isForced404NotFoundRouting(request, requestPath);
    }

    // -----------------------------------------------------
    //                                           URL Mapping
    //                                           -----------
    @Override
    public UrlMappingOption customizeActionUrlMapping(UrlMappingResource resource) {
        return restfulRouter.toRestfulMappingPath(resource).orElseGet(() -> {
            return super.customizeActionUrlMapping(resource);
        });
    }

    @Override
    public UrlReverseOption customizeActionUrlReverse(UrlReverseResource resource) {
        return restfulRouter.toRestfulReversePath(resource).orElseGet(() -> {
            return super.customizeActionUrlReverse(resource);
        });
    }
}