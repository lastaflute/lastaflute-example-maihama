/*
 * Copyright 2015-2022 the original author or authors.
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
package org.docksidestage.app.web.wx.faicli;

import java.util.Map;

import javax.annotation.Resource;

import org.docksidestage.app.web.base.HangarBaseAction;
import org.docksidestage.app.web.base.login.HangarLoginAssist;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.lastaflute.core.exception.LaApplicationException;
import org.lastaflute.web.Execute;
import org.lastaflute.web.login.AllowAnyoneAccess;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author jflute
 */
@AllowAnyoneAccess
public class WxFaicliAction extends HangarBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private HangarLoginAssist loginAssist;
    @Resource
    private MemberBhv memberBhv;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<Map<String, Object>> index(WxFaicliForm form) {
        validate(form, messages -> {});
        Member member = memberBhv.selectByUniqueOf(form.sea).get();
        Map<String, Object> columnMap = member.asDBMeta().extractAllColumnMap(member);
        return asJson(columnMap);
    }

    @Execute
    public JsonResponse<Void> unknown() {
        throw new LaApplicationException("throw unknown application exception") {

            private static final long serialVersionUID = 1L;
        };
    }

    @Execute
    public JsonResponse<Void> entity(String account) {
        memberBhv.selectByUniqueOf(account).get();
        return JsonResponse.asEmptyBody();
    }
}
