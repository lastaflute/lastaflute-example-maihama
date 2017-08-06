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
package org.docksidestage.unit.mock;

import java.util.List;

import org.dbflute.jdbc.ClassificationMeta;
import org.dbflute.optional.OptionalThing;
import org.docksidestage.mylasta.direction.MaihamaFwAssistantDirector;
import org.docksidestage.mylasta.direction.sponsor.MaihamaApiFailureHook;
import org.docksidestage.mylasta.direction.sponsor.MaihamaListedClassificationProvider;
import org.lastaflute.db.dbflute.classification.ListedClassificationProvider;
import org.lastaflute.web.api.ApiFailureHook;
import org.lastaflute.web.api.ApiFailureResource;
import org.lastaflute.web.response.ApiResponse;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author jflute
 */
public class MockMaihamaFwAssistantDirector extends MaihamaFwAssistantDirector {

    @Override
    protected void setupAppConfig(List<String> nameList) {
    }

    @Override
    protected void setupAppMessage(List<String> nameList) {
    }

    @Override
    protected ListedClassificationProvider createListedClassificationProvider() {
        return new MaihamaListedClassificationProvider() {
            protected OptionalThing<ClassificationMeta> onAppCls(String clsName) {
                return OptionalThing.empty();
            }
        };
    }

    @Override
    protected ApiFailureHook createApiFailureHook() {
        return new MaihamaApiFailureHook() {

            @Override
            public ApiResponse handleValidationError(ApiFailureResource resource) {
                return JsonResponse.asEmptyBody();
            }

            @Override
            public ApiResponse handleApplicationException(ApiFailureResource resource, RuntimeException cause) {
                return JsonResponse.asEmptyBody();
            }

            @Override
            public OptionalThing<ApiResponse> handleClientException(ApiFailureResource resource, RuntimeException cause) {
                return OptionalThing.empty();
            }

            @Override
            public OptionalThing<ApiResponse> handleServerException(ApiFailureResource resource, Throwable cause) {
                return OptionalThing.empty();
            }
        };
    }
}
