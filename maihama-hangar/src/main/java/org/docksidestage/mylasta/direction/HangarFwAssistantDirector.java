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
package org.docksidestage.mylasta.direction;

import java.util.List;

import org.docksidestage.mylasta.direction.sponsor.HangarListedClassificationProvider;
import org.lastaflute.db.dbflute.classification.ListedClassificationProvider;
import org.lastaflute.web.direction.FwWebDirection;
import org.lastaflute.web.servlet.filter.cors.CorsHook;

/**
 * @author jflute
 */
public class HangarFwAssistantDirector extends MaihamaFwAssistantDirector {

    @Override
    protected void setupAppConfig(List<String> nameList) {
        nameList.add("hangar_config.properties"); // base point
        nameList.add("hangar_env.properties");
    }

    @Override
    protected void setupAppMessage(List<String> nameList) {
        nameList.add("hangar_message"); // base point
        nameList.add("hangar_label");
    }

    @Override
    protected ListedClassificationProvider createListedClassificationProvider() {
        return new HangarListedClassificationProvider();
    }

    @Override
    protected void prepareWebDirection(FwWebDirection direction) {
        super.prepareWebDirection(direction);
        final String allowOrigin = "http://localhost:3000"; // #simple_for_example should be environment configuration
        direction.directCors(new CorsHook(allowOrigin)); // #change_it
    }
}
