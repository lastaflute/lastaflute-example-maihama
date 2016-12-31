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

import javax.annotation.Resource;

import org.docksidestage.app.web.base.HangarBaseAction;
import org.docksidestage.app.web.base.login.HangarLoginAssist;
import org.lastaflute.web.Execute;
import org.lastaflute.web.login.AllowAnyoneAccess;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author jflute
 * @author iwamatsu0430
 */
public class RootAction extends HangarBaseAction {

    @Resource
    private HangarLoginAssist docksideLoginAssist;

    @AllowAnyoneAccess
    @Execute
    public JsonResponse<Void> index() {
        return JsonResponse.asEmptyBody();
    }

    @Execute
    public JsonResponse<Void> signout() {
        docksideLoginAssist.logout();
        return JsonResponse.asEmptyBody();
    }
}
