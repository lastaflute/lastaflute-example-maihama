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

import org.docksidestage.mylasta.direction.sponsor.ShowbaseActionAdjustmentProvider;
import org.docksidestage.mylasta.direction.sponsor.ShowbaseApiFailureHook;
import org.docksidestage.mylasta.direction.sponsor.ShowbaseListedClassificationProvider;
import org.lastaflute.db.dbflute.classification.ListedClassificationProvider;
import org.lastaflute.web.api.ApiFailureHook;
import org.lastaflute.web.path.ActionAdjustmentProvider;

/**
 * @author jflute
 */
public class ShowbaseFwAssistantDirector extends MaihamaFwAssistantDirector {

    @Resource
    private ShowbaseConfig config;

    @Override
    protected void setupAppConfig(List<String> nameList) {
        nameList.add("showbase_config.properties"); // base point
        nameList.add("showbase_env.properties");
    }

    @Override
    protected void setupAppMessage(List<String> nameList) {
        nameList.add("showbase_message"); // base point
        nameList.add("showbase_label");
    }

    @Override
    protected ListedClassificationProvider createListedClassificationProvider() {
        return new ShowbaseListedClassificationProvider();
    }

    @Override
    protected ActionAdjustmentProvider createActionAdjustmentProvider() {
        return new ShowbaseActionAdjustmentProvider(config);
    }

    @Override
    protected ApiFailureHook createApiFailureHook() {
        return new ShowbaseApiFailureHook(); // for client-managed message
    }
}
