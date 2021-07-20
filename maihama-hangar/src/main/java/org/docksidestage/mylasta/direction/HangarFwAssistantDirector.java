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
package org.docksidestage.mylasta.direction;

import java.util.List;

import javax.annotation.Resource;

import org.docksidestage.mylasta.direction.sponsor.HangarActionAdjustmentProvider;
import org.docksidestage.mylasta.direction.sponsor.HangarApiFailureHook;
import org.docksidestage.mylasta.direction.sponsor.HangarListedClassificationProvider;
import org.docksidestage.mylasta.direction.sponsor.planner.HangarActionOptionPlanner;
import org.lastaflute.db.dbflute.classification.ListedClassificationProvider;
import org.lastaflute.web.api.ApiFailureHook;
import org.lastaflute.web.direction.FwWebDirection;
import org.lastaflute.web.path.ActionAdjustmentProvider;
import org.lastaflute.web.servlet.filter.cors.CorsHook;

/**
 * @author jflute
 */
public class HangarFwAssistantDirector extends MaihamaFwAssistantDirector {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private HangarConfig config;

    // ===================================================================================
    //                                                                              Assist
    //                                                                              ======
    @Override
    protected void setupAppConfig(List<String> nameList) {
        nameList.add("hangar_config.properties"); // base point
        nameList.add("hangar_env.properties");
    }

    // ===================================================================================
    //                                                                               Core
    //                                                                              ======
    @Override
    protected void setupAppMessage(List<String> nameList) {
        nameList.add("hangar_message"); // base point
        nameList.add("hangar_label");
    }

    // ===================================================================================
    //                                                                                 DB
    //                                                                                ====
    @Override
    protected ListedClassificationProvider createListedClassificationProvider() {
        return new HangarListedClassificationProvider();
    }

    // ===================================================================================
    //                                                                                Web
    //                                                                               =====
    @Override
    protected void prepareWebDirection(FwWebDirection direction) {
        super.prepareWebDirection(direction);
        direction.directCors(createCorsHook()); // #change_it if you don't use CORS, delete this
    }

    @Override
    protected ActionAdjustmentProvider createActionAdjustmentProvider() {
        return new HangarActionAdjustmentProvider(createActionOptionPlanner());
    }

    protected HangarActionOptionPlanner createActionOptionPlanner() {
        return new HangarActionOptionPlanner(config);
    }

    @Override
    protected ApiFailureHook createApiFailureHook() {
        return new HangarApiFailureHook();
    }

    protected CorsHook createCorsHook() { // #change_it if you don't use CORS, delete this
        String allowOrigin = "http://localhost:3000"; // #simple_for_example should be environment configuration
        return new CorsHook(allowOrigin);
    }
}
