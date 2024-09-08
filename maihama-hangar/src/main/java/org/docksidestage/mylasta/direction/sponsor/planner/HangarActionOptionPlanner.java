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
package org.docksidestage.mylasta.direction.sponsor.planner;

import java.util.function.Predicate;

import org.docksidestage.mylasta.direction.HangarConfig;
import org.lastaflute.web.path.restful.router.NumericBasedRestfulRouter;
import org.lastaflute.web.path.restful.router.RestfulRouter;

/**
 * @author jflute
 */
public class HangarActionOptionPlanner {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final HangarConfig config;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public HangarActionOptionPlanner(HangarConfig config) {
        this.config = config;
    }

    // ===================================================================================
    //                                                                             Routing
    //                                                                             =======
    // -----------------------------------------------------
    //                                               Swagger
    //                                               -------
    public Predicate<String> createDisabledSwaggerDeterminer() {
        return requestPath -> !config.isSwaggerEnabled() && isSwaggerRequest(requestPath); // e.g. swagger's html, css
    }

    private boolean isSwaggerRequest(String requestPath) {
        return requestPath.startsWith("/webjars/swagger-ui") || requestPath.startsWith("/swagger");
    }

    // -----------------------------------------------------
    //                                               Restful
    //                                               -------
    public RestfulRouter createRestfulRouter() {
        return new NumericBasedRestfulRouter();
    }
}