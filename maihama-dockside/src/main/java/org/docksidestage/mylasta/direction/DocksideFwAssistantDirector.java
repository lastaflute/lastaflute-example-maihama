/*
 * Copyright 2014-2015 the original author or authors.
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

import org.docksidestage.mylasta.direction.sponsor.DocksideMultipartRequestHandler;
import org.lastaflute.web.direction.FwWebDirection;
import org.lastaflute.web.ruts.multipart.MultipartRequestHandler;
import org.lastaflute.web.ruts.multipart.MultipartResourceProvider;

/**
 * @author jflute
 */
public class DocksideFwAssistantDirector extends MaihamaFwAssistantDirector {

    @Override
    protected void setupAppConfig(List<String> nameList) {
        nameList.add("dockside_config.properties"); // base point
        nameList.add("dockside_env.properties");
    }

    @Override
    protected void setupAppMessage(List<String> nameList) {
        nameList.add("dockside_message"); // base point
        nameList.add("dockside_label");
    }

    @Override
    protected void prepareWebDirection(FwWebDirection direction) {
        super.prepareWebDirection(direction);
        direction.directMultipart(new MultipartResourceProvider() {
            @Override
            public MultipartRequestHandler createHandler() {
                return new DocksideMultipartRequestHandler();
            }
        });
    }
}
