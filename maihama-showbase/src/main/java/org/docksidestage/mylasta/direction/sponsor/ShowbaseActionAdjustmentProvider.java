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
package org.docksidestage.mylasta.direction.sponsor;

import javax.servlet.http.HttpServletRequest;

import org.docksidestage.mylasta.direction.ShowbaseConfig;

/**
 * @author jflute
 */
public class ShowbaseActionAdjustmentProvider extends MaihamaActionAdjustmentProvider {

    private final ShowbaseConfig config;

    public ShowbaseActionAdjustmentProvider(ShowbaseConfig config) {
        this.config = config;
    }

    @Override
    public boolean isForcedRoutingExcept(HttpServletRequest request, String requestPath) {
        if (!config.isDevelopmentHere() && requestPath.startsWith("/swagger")) { // e.g. swagger in production
            return true;
        }
        return super.isForcedRoutingExcept(request, requestPath);
    }
}