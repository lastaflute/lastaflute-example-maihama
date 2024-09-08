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
package org.docksidestage.mylasta.direction;

import java.util.List;

import org.docksidestage.mylasta.direction.sponsor.ShowbaseActionAdjustmentProvider;
import org.docksidestage.mylasta.direction.sponsor.ShowbaseApiFailureHook;
import org.docksidestage.mylasta.direction.sponsor.ShowbaseListedClassificationProvider;
import org.docksidestage.mylasta.direction.sponsor.planner.ShowbaseActionOptionPlanner;
import org.lastaflute.db.dbflute.classification.ListedClassificationProvider;
import org.lastaflute.web.api.ApiFailureHook;
import org.lastaflute.web.path.ActionAdjustmentProvider;

import jakarta.annotation.Resource;

/**
 * @author jflute
 */
public class ShowbaseFwAssistantDirector extends MaihamaFwAssistantDirector {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private ShowbaseConfig config;

    // ===================================================================================
    //                                                                              Assist
    //                                                                              ======
    @Override
    protected void setupAppConfig(List<String> nameList) {
        nameList.add("showbase_config.properties"); // base point
        nameList.add("showbase_env.properties");
    }

    // ===================================================================================
    //                                                                               Core
    //                                                                              ======
    @Override
    protected void setupAppMessage(List<String> nameList) {
        nameList.add("showbase_message"); // base point
        nameList.add("showbase_label");
    }

    // ===================================================================================
    //                                                                                 DB
    //                                                                                ====
    @Override
    protected ListedClassificationProvider createListedClassificationProvider() {
        return new ShowbaseListedClassificationProvider();
    }

    // ===================================================================================
    //                                                                                Web
    //                                                                               =====
    @Override
    protected ActionAdjustmentProvider createActionAdjustmentProvider() {
        return new ShowbaseActionAdjustmentProvider(createActionOptionPlanner());
    }

    protected ShowbaseActionOptionPlanner createActionOptionPlanner() {
        return new ShowbaseActionOptionPlanner(config);
    }

    @Override
    protected ApiFailureHook createApiFailureHook() {
        return new ShowbaseApiFailureHook(); // for client-managed message
    }
}
